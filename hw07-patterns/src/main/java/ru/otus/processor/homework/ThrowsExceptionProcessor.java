package ru.otus.processor.homework;

import ru.otus.exception.ProcessorException;
import ru.otus.model.Message;
import ru.otus.processor.Processor;

public class ThrowsExceptionProcessor implements Processor {

    public ThrowsExceptionProcessor(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
    }

    private final TimeProvider timeProvider;

    @Override
    public Message process(Message message) {
        if (timeProvider.getTime().getSecond() % 2 == 0) {
            throw new ProcessorException("Current message was processed at even second");
        }
        return message;
    }
}
