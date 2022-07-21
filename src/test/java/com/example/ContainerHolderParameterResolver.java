package com.example;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ContainerHolderParameterResolver implements Extension, BeforeEachCallback, AfterEachCallback, BeforeAllCallback, AfterAllCallback, ParameterResolver, TestExecutionExceptionHandler{
    private final Map<Object, ContainerHolder> testInstanceContainerMap = new HashMap<>();
    private final Set<Object> testInstancesWithExceptionOrFailure = new HashSet<>();
    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        if (TestInstance.Lifecycle.PER_CLASS == context.getTestInstanceLifecycle().orElse(null)) {
            discardContainers(context);
        }
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        if (TestInstance.Lifecycle.PER_METHOD == context.getTestInstanceLifecycle().orElse(null)) {
            discardContainers(context);
        }
    }

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        if (TestInstance.Lifecycle.PER_CLASS == context.getTestInstanceLifecycle().orElse(null)) {
            setupContainers(context);
        }
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        if (TestInstance.Lifecycle.PER_METHOD == context.getTestInstanceLifecycle().orElse(null)) {
            setupContainers(context);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return ContainerHolder.class.equals(parameterContext.getParameter().getType());
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return testInstanceContainerMap.get(extensionContext.getRequiredTestInstance());
    }

    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        testInstancesWithExceptionOrFailure.add(context.getRequiredTestInstance());
        throw throwable;
    }

    private void setupContainers(ExtensionContext context) {
        if (!testInstanceContainerMap.containsKey(context.getRequiredTestInstance())) {
            final ContainerHolder containerHolder = new ContainerHolder();
            testInstanceContainerMap.put(context.getRequiredTestInstance(), containerHolder);
            System.out.println("New context with container started within thread " + Thread.currentThread().getName());
        }
    }

    private void discardContainers(ExtensionContext context) {
        final Object requiredTestInstance = context.getRequiredTestInstance();
        final ContainerHolder containerHolder = testInstanceContainerMap.get(requiredTestInstance);
        if(context.getExecutionException().isPresent() || testInstancesWithExceptionOrFailure.contains(requiredTestInstance)) {
            containerHolder.printLogs();
        }
        containerHolder.tearDown();
        testInstanceContainerMap.remove(requiredTestInstance);
        System.out.println("Context with container ended within thread " + Thread.currentThread().getName());
    }
}
