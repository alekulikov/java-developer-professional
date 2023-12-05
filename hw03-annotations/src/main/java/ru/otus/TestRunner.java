package ru.otus;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;

public class TestRunner {

    private static final Logger logger = LoggerFactory.getLogger(TestRunner.class);

    public static void main(String[] args) {
        try {
            runTestClass("ru.otus.TestClass");
        } catch (ClassNotFoundException e) {
            logger.error("Class does not exist exist:", e);
        }
    }

    private static void runTestClass(String testClass) throws ClassNotFoundException {
        var clazz = Class.forName(testClass);
        var beforeMethods = getAllMethodsByAnnotation(clazz, Before.class);
        var afterMethods = getAllMethodsByAnnotation(clazz, After.class);
        var testMethods = getAllMethodsByAnnotation(clazz, Test.class);

        var testPassedCount = testMethods.stream()
                .mapToInt(test ->
                        Boolean.compare(runTest(test, beforeMethods, afterMethods, getTestObject(clazz)), false))
                .sum();
        logger.info(
                "{} TESTS PASSED | {} TESTS FAILED | {} TESTS TOTAL",
                testPassedCount,
                testMethods.size() - testPassedCount,
                testMethods.size());
    }

    private static List<Method> getAllMethodsByAnnotation(Class<?> clazz, Class<? extends Annotation> annotation) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(annotation))
                .toList();
    }

    private static boolean runTest(
            Method test, List<Method> beforeMethods, List<Method> afterMethods, Object testObject) {
        var testResult = true;
        runMethods(beforeMethods, testObject);
        logger.info("{} is running", test.getDeclaredAnnotation(Test.class).displayName());
        try {
            test.invoke(testObject);
        } catch (Exception e) {
            logger.error("Test failed", e);
            testResult = false;
        }
        runMethods(afterMethods, testObject);
        return testResult;
    }

    private static void runMethods(List<Method> methods, Object testObject) {
        for (Method method : methods) {
            try {
                method.invoke(testObject);
            } catch (Exception e) {
                logger.warn("Warning! {} method failed! Results can be invalid", method);
            }
        }
    }

    private static Object getTestObject(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            logger.warn("Warning! Object {} not created! Results can be invalid", clazz);
            return new Object();
        }
    }
}
