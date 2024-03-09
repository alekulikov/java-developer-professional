package ru.otus.appcontainer.exception;

public class AppComponentContainerException extends RuntimeException {
    public AppComponentContainerException(String message) {
        super(message);
    }

    public AppComponentContainerException(String message, Exception cause) {
        super(message, cause);
    }
}
