package ru.otus.exceptions;

public class TestFailedException extends RuntimeException {
    public TestFailedException() {}

    public TestFailedException(String message) {
        super(message);
    }

    public TestFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public TestFailedException(Throwable cause) {
        super(cause);
    }
}
