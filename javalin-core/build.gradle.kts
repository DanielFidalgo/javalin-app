plugins {
    kotlin("jvm") version "1.8.20"
}

group = "io.github.danielfidalgo"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    api(libs.javalin)
    api(libs.javalin.openapi)
    api(libs.javalin.swagger)
    api(libs.micrometer)
    api(libs.slf4j)
    testImplementation(testLibs.jupiter.api)
    testRuntimeOnly(testLibs.jupiter.engine)
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}
