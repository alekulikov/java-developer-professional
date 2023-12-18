package ru.otus.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.annotation.Log;

public class TestLogging implements TestLoggingInterface {
    private static final Logger logger = LoggerFactory.getLogger(TestLogging.class);
    private static final String LOG_MESSAGE_PATTERN = "Calculation result: {}";

    @Log
    public void calculation() {
        logger.info(LOG_MESSAGE_PATTERN, "NaN");
    }

    @Log
    public void calculation(int param1) {
        logger.info(LOG_MESSAGE_PATTERN, param1);
    }

    @Log
    public void calculation(int param1, int param2) {
        logger.info(LOG_MESSAGE_PATTERN, param1 + param2);
    }

    @Log
    public void calculation(int param1, int param2, String param3) {
        try {
            logger.info(LOG_MESSAGE_PATTERN, param1 + param2 + Integer.parseInt(param3));
        } catch (NumberFormatException e) {
            logger.error(e.getMessage());
        }
    }
}
