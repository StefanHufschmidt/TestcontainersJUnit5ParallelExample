package com.example;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(ContainerHolderParameterResolver.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public @interface FreshContainersPerTestMethod {
}
