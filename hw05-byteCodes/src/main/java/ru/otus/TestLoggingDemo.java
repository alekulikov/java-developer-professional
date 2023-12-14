package ru.otus;

import ru.otus.aop.Ioc;
import ru.otus.example.TestLoggingInterface;

public class TestLoggingDemo {
    public static void main(String[] args) {
        TestLoggingInterface testLogging = Ioc.createTestLogging();
        testLogging.calculation();
        testLogging.calculation(5);
        testLogging.calculation(5, -7);
        testLogging.calculation(5, -7, "2");
    }
}
