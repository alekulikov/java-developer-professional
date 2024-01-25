package ru.otus.jdbc.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import ru.otus.jdbc.annotation.Id;
import ru.otus.jdbc.exception.ClassMetaDataException;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    private final String name;
    private final Constructor<T> constructor;

    private final Field idField;

    private final List<Field> allFields;

    private final List<Field> fieldsWithoutId;

    public EntityClassMetaDataImpl(Class<T> clazz) {
        this.name = clazz.getSimpleName().toLowerCase();
        this.idField = Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Id.class))
                .findFirst()
                .orElseThrow(() -> new ClassMetaDataException("one of the class fields must contain @Id"));
        this.allFields = Arrays.stream(clazz.getDeclaredFields()).collect(Collectors.toCollection(LinkedList::new));
        this.fieldsWithoutId = Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> !field.isAnnotationPresent(Id.class))
                .collect(Collectors.toCollection(LinkedList::new));
        try {
            this.constructor =
                    clazz.getConstructor(allFields.stream().map(Field::getType).toArray(Class[]::new));
        } catch (NoSuchMethodException e) {
            throw new ClassMetaDataException(e);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Constructor<T> getConstructor() {
        return constructor;
    }

    @Override
    public Field getIdField() {
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        return allFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return fieldsWithoutId;
    }
}
