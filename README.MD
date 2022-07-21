# Testcontainers JUnit 5 parallel example
This example shows a possible configuration to use Testcontainers for parallel test execution
with JUnit 5. Running tests in parallel requires clean test contexts for all parallel running tests. To ensure
having clean containers for the specific use case we're using some JUnit5 extensions in combination with self defined
annotations.

## Component explanation
* [FreshContainersPerTestClass](src/test/java/com/example/FreshContainersPerTestClass.java)
        Annotation to use when require a fresh container for each test class. Not all test classes require a complete 
fresh container setup for each method.
* [FreshContainersPerTestMethod](src/test/java/com/example/FreshContainersPerTestMethod.java)
        Annotation to use when require a fresh container for each test method. At some test classes we require a 
complete fresh container setup for each method to avoid test pollution.
* [ContainerHolderParameterResolver](src/test/java/com/example/ContainerHolderParameterResolver.java)
        Resolves the parameter for the `ContainerHolder` used within the specific test context.
* [ContainerHolder](src/test/java/com/example/ContainerHolder.java)
        A holder object to store all your required Testcontainers at. In this example we're 
only using one `MockServerContainer` but this could basically contain your whole setup.
 
## Configure parallelism
On systems where you'll execute your tests you'll not find infinite resources. Since you're spinning up several 
containers in parallel when running your tests in parallel you might use more resources than regularly. Therefore, 
a good configuration is recommended. You can define the amount of parallel test executions by configuring the property
`junit.jupiter.execution.parallel.config.fixed.parallelism` inside your 
[junit-platform.properties](src/test/resources/junit-platform.properties).
