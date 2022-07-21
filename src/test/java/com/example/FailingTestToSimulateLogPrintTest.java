package com.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

@FreshContainersPerTestClass
public class FailingTestToSimulateLogPrintTest {

    @Test
    public void failingTest(ContainerHolder containerHolder) {
        System.out.println("Failing on endpoint: " + containerHolder.getWebserver().getEndpoint());
        fail("This tests fails on purpose to let you see the log print of the container.");
    }
}
