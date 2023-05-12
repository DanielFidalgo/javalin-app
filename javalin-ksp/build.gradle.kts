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
    implementation(libs.ksp.processing.api)
    implementation(libs.kotlinPoet)
    testImplementation(testLibs.jupiter.api)
    testRuntimeOnly(testLibs.jupiter.engine)
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}