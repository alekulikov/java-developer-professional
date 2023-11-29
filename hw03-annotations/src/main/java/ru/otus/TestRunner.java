package ru.otus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;
import ru.otus.exceptions.TestFailedException;

public class TestRunner {

    private static final Logger logger = LoggerFactory.getLogger(TestRunner.class);
    private static final String EXCEPTION_MESSAGE = "Annotations cannot be specified over a single method";

    public static void main(String[] args) {
        try {
            runTestClass("ru.otus.TestClass");
        } catch (ClassNotFoundException e) {
            logger.error("Class does not exist exist:", e);
        }
    }

    private static void runTestClass(String testClass) throws ClassNotFoundException {
        var clazz = Class.forName(testClass);
        var beforeMethods = getAllBeforeMethods(clazz);
        var afterMethods = getAllAfterMethods(clazz);

        Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(Test.class))
                .forEach(test -> runTest(test, beforeMethods, afterMethods));
    }

    private static List<Method> getAllBeforeMethods(Class<?> clazz) {
        var beforeMethods = Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(Before.class))
                .toList();

        beforeMethods.forEach(method -> {
            if (Arrays.stream(method.getDeclaredAnnotations()).anyMatch(annotation -> {
                var annotationType = annotation.annotationType();
                return annotationType.equals(Test.class) || annotationType.equals(After.class);
            })) {
                throw new TestFailedException(EXCEPTION_MESSAGE);
            }
        });

        return beforeMethods;
    }

    private static List<Method> getAllAfterMethods(Class<?> clazz) {
        var afterMethods = Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(After.class))
                .toList();

        afterMethods.forEach(method -> {
            if (Arrays.stream(method.getDeclaredAnnotations()).anyMatch(annotation -> {
                var annotationType = annotation.annotationType();
                return annotationType.equals(Test.class) || annotationType.equals(Before.class);
            })) {
                throw new TestFailedException(EXCEPTION_MESSAGE);
            }
        });

        return afterMethods;
    }

    private static void runTest(Method test, List<Method> beforeMethods, List<Method> afterMethods) {
        beforeMethods.forEach(TestRunner::invokeWithoutArguments);
        logger.info("{} is running", test.getDeclaredAnnotation(Test.class).displayName());
        invokeWithoutArguments(test);
        afterMethods.forEach(TestRunner::invokeWithoutArguments);
    }

    private static void invokeWithoutArguments(Method method) {
        try {
            method.invoke(null);
        } catch (IllegalAccessException e) {
            logger.error("Cannot access a test method", e);
        } catch (InvocationTargetException e) {
            logger.error("Failed to invoke method", e);
        }
    }
}
