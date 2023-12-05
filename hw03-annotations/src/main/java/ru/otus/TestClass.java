package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;
import ru.otus.exceptions.TestFailedException;

public final class TestClass {
    private static final Logger logger = LoggerFactory.getLogger(TestClass.class);

    @Before
    public void beforeFirstMethod() {
        logger.info("beforeFirstMethod running");
    }

    @Before
    public void beforeSecondMethod() {
        logger.info("beforeSecondMethod running");
    }

    @After
    public void afterMethod() {
        logger.info("afterMethod running");
    }

    @Test(displayName = "Test1")
    public void test1() {
        throw new TestFailedException("Test1 failed");
    }

    @Test(displayName = "Test2")
    public void test2() {
        logger.info("Test2 success");
    }

    @Test(displayName = "Test3")
    private void test3() {
        logger.info("Test3 success");
    }
}
