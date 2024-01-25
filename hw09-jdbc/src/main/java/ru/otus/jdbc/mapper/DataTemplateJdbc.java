package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
@SuppressWarnings({"java:S3011"})
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(
            DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData<T> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), rs -> {
            try {
                if (rs.next()) {
                    return entityClassMetaData.getConstructor().newInstance(getValues(rs));
                }
                return null;
            } catch (SQLException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new DataTemplateException(e);
            }
        });
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor
                .executeSelect(connection, entitySQLMetaData.getSelectAllSql(), Collections.emptyList(), rs -> {
                    var resultList = new ArrayList<T>();
                    try {
                        while (rs.next()) {
                            resultList.add(entityClassMetaData.getConstructor().newInstance(getValues(rs)));
                        }
                        return resultList;
                    } catch (SQLException
                            | InstantiationException
                            | IllegalAccessException
                            | InvocationTargetException e) {
                        throw new DataTemplateException(e);
                    }
                })
                .orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    @Override
    public long insert(Connection connection, T client) {
        var fieldsWithoutId = entityClassMetaData.getFieldsWithoutId();
        var fieldsValues = new LinkedList<>();

        try {
            for (Field field : fieldsWithoutId) {
                field.setAccessible(true);
                fieldsValues.add(field.get(client));
            }
            return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), fieldsValues);
        } catch (IllegalAccessException e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T client) {
        var fieldsWithoutId = entityClassMetaData.getFieldsWithoutId();
        var fieldsValues = new LinkedList<>();
        var fieldId = entityClassMetaData.getIdField();
        fieldId.setAccessible(true);

        try {
            for (Field field : fieldsWithoutId) {
                field.setAccessible(true);
                fieldsValues.add(field.get(client));
            }
            fieldsValues.add(fieldId.get(client));
            dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), fieldsValues);
        } catch (IllegalAccessException e) {
            throw new DataTemplateException(e);
        }
    }

    private Object[] getValues(ResultSet rs) throws SQLException {
        var fieldsNames = entityClassMetaData.getAllFields().stream()
                .map(Field::getName)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        var fieldValues = new LinkedList<>();
        for (String fieldName : fieldsNames) {
            fieldValues.add(rs.getObject(fieldName));
        }
        return fieldValues.toArray();
    }
}
