package ru.otus.processor.homework;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.exception.ProcessorException;
import ru.otus.model.Message;

class ExceptionProcessorTest {
    private ThrowsExceptionProcessor processor;
    private final Message message = new Message.Builder(1L).field1("test").build();

    @Test
    @DisplayName("Process throws ProcessException at even seconds")
    void processEndsWithException() {
        processor = new ThrowsExceptionProcessor(() -> LocalTime.ofSecondOfDay(3000));
        assertThrowsExactly(
                ProcessorException.class,
                () -> processor.process(message),
                "Current message was processed at even second");
    }

    @Test
    @DisplayName("Processor does not throw ProcessException at odd seconds")
    void processEndsWithoutException() {
        processor = new ThrowsExceptionProcessor(() -> LocalTime.ofSecondOfDay(3003));
        assertDoesNotThrow(() -> processor.process(message));
    }
}
