package com.example;

import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.utility.DockerImageName;

public class ContainerHolder {

    private final MockServerContainer webserver;

    public ContainerHolder() {
        webserver = new MockServerContainer(DockerImageName.parse("jamesdbloom/mockserver:mockserver-5.13.2"));
        webserver.start();
        System.out.println("Started Mockserver in Thread " + Thread.currentThread().getName());
    }

    public void tearDown() {
        System.out.println("Shutting down Mockserver in Thread " + Thread.currentThread().getName());
        webserver.close();
    }

    public void printLogs() {
        System.out.println(webserver.getLogs());
    }

    public MockServerContainer getWebserver() {
        return webserver;
    }
}
