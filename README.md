# JAVALIN APP

A multimodule kotlin jvm app building native  with GraalVM

## Stack
#### JVM: Java 17
#### Web : [Javalin](https://javalin.io) 
#### Serialization: [Kotlinx.Serialization](https://kotlinlang.org/api/kotlinx.serialization/)
#### Dependency Injection: [Dagger2](https://dagger.dev/)
#### Persistence: [Flyway](https://flywaydb.org/) + [JOOQ](https://www.jooq.org/) 

## Running the application
To start on standard jvm:
```shell
./gradlew clean run
```

### Preparing for Native Build
Your Application may rely on some reflections from dependencies. 
This app can execute with GraalVM's tracing agent to support these cases.
To run with the agent:
```shell
./gradlew clean run -PgraalvmAgent
```
This will store the configuration files under [META-INF/native-config](service/src/main/resources/META-INF/native-config) in the service Module