package ru.otus.appcontainer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.appcontainer.exception.AppComponentContainerException;

@SuppressWarnings("squid:S1068")
public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);
        var config = getConfigObject(configClass);
        var componentsInitMethods = Arrays.stream(configClass.getMethods())
                .filter(method -> method.isAnnotationPresent(AppComponent.class))
                .sorted(Comparator.comparingInt(
                        method -> method.getAnnotation(AppComponent.class).order()))
                .toList();
        for (Method componentInitMethod : componentsInitMethods) {
            var componentName =
                    componentInitMethod.getAnnotation(AppComponent.class).name();
            if (!appComponentsByName.containsKey(componentName)) {
                var component = initComponent(componentInitMethod, config);
                appComponents.add(component);
                appComponentsByName.put(componentName, component);
            } else {
                throw new AppComponentContainerException(
                        String.format("Component with name %s already exist", componentName));
            }
        }
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    private Object getConfigObject(Class<?> configClass) {
        try {
            return configClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new AppComponentContainerException(
                    String.format("Config %s cannot be create", configClass.getName()), e);
        }
    }

    private Object initComponent(Method componentInitMethod, Object config) {
        var parameters = Arrays.stream(componentInitMethod.getParameterTypes())
                .map(this::getAppComponent)
                .toArray();
        try {
            return componentInitMethod.invoke(config, parameters);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new AppComponentContainerException(
                    String.format("Method %s cannot be invoke", componentInitMethod.getName()), e);
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        var components =
                appComponents.stream().filter(componentClass::isInstance).toList();
        if (components.isEmpty()) {
            throw new AppComponentContainerException(String.format("Component %s not found", componentClass.getName()));
        } else if (components.size() > 1) {
            throw new AppComponentContainerException(
                    String.format("More than one component %s found", componentClass.getName()));
        }
        return (C) components.get(0);
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        if (appComponentsByName.containsKey(componentName)) {
            return (C) appComponentsByName.get(componentName);
        } else {
            throw new AppComponentContainerException(String.format("Component %s not found", componentName));
        }
    }
}
