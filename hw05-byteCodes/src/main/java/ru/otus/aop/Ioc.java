package ru.otus.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.annotation.Log;

public class Ioc {
    private static final Logger logger = LoggerFactory.getLogger(Ioc.class);

    private Ioc() {}

    public static <T, S extends T> T createTestLogging(Class<T> classInterface, S classObject) {
        InvocationHandler handler = new CalculationInvocationHandler<>(classObject);
        return classInterface.cast(Proxy.newProxyInstance(
                Ioc.class.getClassLoader(), classObject.getClass().getInterfaces(), handler));
    }

    static class CalculationInvocationHandler<T> implements InvocationHandler {
        private final T testLogging;
        private final Set<String> logMethodsSignatures;

        CalculationInvocationHandler(T testLogging) {
            this.testLogging = testLogging;
            this.logMethodsSignatures = getLogMethodsSignatures();
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (logMethodsSignatures.contains(getMethodSignature(method))) {
                String params = Arrays.toString(args);
                logger.info("Method called with arguments: {}", params);
            }
            return method.invoke(testLogging, args);
        }

        private String getMethodSignature(Method method) {
            return method.getName() + Arrays.toString(method.getParameterTypes());
        }

        private Set<String> getLogMethodsSignatures() {
            return Arrays.stream(testLogging.getClass().getDeclaredMethods())
                    .filter(method -> method.isAnnotationPresent(Log.class))
                    .map(this::getMethodSignature)
                    .collect(Collectors.toSet());
        }
    }
}
