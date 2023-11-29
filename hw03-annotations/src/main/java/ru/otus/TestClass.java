package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;
import ru.otus.exceptions.TestFailedException;

public final class TestClass {
    private static final Logger logger = LoggerFactory.getLogger(TestClass.class);

    private TestClass() {}

    @Before
    public static void beforeFirstMethod() {
        logger.info("beforeFirstMethod running");
    }

    @Before
    public static void beforeSecondMethod() {
        logger.info("beforeSecondMethod running");
    }

    @After
    public static void afterMethod() {
        logger.info("afterMethod running");
    }

    @Test(displayName = "Test1")
    public static void test1() {
        try {
            throw new TestFailedException("Test1 failed");
        } catch (TestFailedException e) {
            logger.error("Test Failed:", e);
        }
    }

    @Test(displayName = "Test2")
    public static void test2() {
        logger.info("Test2 success");
    }

    @Test(displayName = "Test3")
    private static void test3() {
        logger.info("Test3 success");
    }
}
