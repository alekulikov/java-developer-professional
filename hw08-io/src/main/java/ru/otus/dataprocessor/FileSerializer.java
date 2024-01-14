package ru.otus.dataprocessor;

import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

public class FileSerializer implements Serializer {

    private final String fileName;

    public FileSerializer(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void serialize(Map<String, Double> data) {
        // формирует результирующий json и сохраняет его в файл
        try (var outputStream = new FileOutputStream(fileName)) {
            var objectMapper = JsonMapper.builder().build();
            objectMapper.writeValue(outputStream, data);
        } catch (IOException e) {
            throw new FileProcessException(e);
        }
    }
}
