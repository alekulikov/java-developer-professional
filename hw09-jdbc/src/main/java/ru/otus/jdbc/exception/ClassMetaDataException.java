package ru.otus.jdbc.exception;

public class ClassMetaDataException extends RuntimeException {
    public ClassMetaDataException(String message) {
        super(message);
    }

    public ClassMetaDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClassMetaDataException(Throwable cause) {
        super(cause);
    }
}
