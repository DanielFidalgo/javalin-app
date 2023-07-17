
rootProject.name = "javalin-app"
include("service")
include("javalin-core")
include("javalin-ksp")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            library("javalin", "io.javalin:javalin:5.6.1")
            library("javalin.openapi", "io.javalin.community.openapi:javalin-openapi-plugin:5.6.1")
            library("javalin.swagger", "io.javalin.community.openapi:javalin-swagger-plugin:5.6.1")
            library("javalin.openapi.processor","io.javalin.community.openapi:openapi-annotation-processor:5.6.1")
            library("micrometer", "io.micrometer:micrometer-core:1.11.1")
            library("micrometer.prometheus", "io.micrometer:micrometer-registry-prometheus:1.11.1")
            library("slf4j", "org.slf4j:slf4j-simple:2.0.5")
            library("dagger", "com.google.dagger:dagger:2.46.1")
            library("dagger.compiler", "com.google.dagger:dagger-compiler:2.46.1")
            library("kotlinx.serialization.json", "org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
            library("kotlinx.serialization.protobuf", "org.jetbrains.kotlinx:kotlinx-serialization-protobuf:1.5.1")
            library("kotlinx.coroutines", "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.2")
            library("ksp.processing.api", "com.google.devtools.ksp:symbol-processing-api:1.9.0-1.0.11")
            library("kotlinPoet", "com.squareup:kotlinpoet-ksp:1.12.0")
            library("jooq", "org.jooq:jooq:3.18.5")
            library("agroal", "io.agroal:agroal-pool:2.2")
            library("flyway", "org.flywaydb:flyway-core:9.20.0")
            library("sql","com.github.fidalgotech:sql:230717-031827")
        }

        create("testLibs") {
            library("jupiter", "org.junit.jupiter:junit-jupiter:5.9.2")
            library("jupiter.api", "org.junit.jupiter:junit-jupiter-api:5.9.2")
            library("jupiter.engine", "org.junit.jupiter:junit-jupiter-engine:5.9.2")
        }
    }
}




