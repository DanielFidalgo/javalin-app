plugins {
    kotlin("jvm") version "1.8.20"
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    api(libs.jooq)
    api(libs.agroal)
    api(libs.flyway)
}

kotlin {
    jvmToolchain(17)
}