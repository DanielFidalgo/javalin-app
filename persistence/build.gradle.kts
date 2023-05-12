plugins {
    kotlin("jvm") version "1.8.20"
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    api("org.jooq:jooq:3.18.3")
    api("io.agroal:agroal-pool:2.1")
    api("org.flywaydb:flyway-core:9.16.0")
}

kotlin {
    jvmToolchain(17)
}



