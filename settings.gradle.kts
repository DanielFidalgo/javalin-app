
rootProject.name = "javalin-app"
include("service")
include("javalin-core")
include("javalin-ksp")
include("persistence")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            library("javalin", "io.javalin:javalin:5.5.0")
            library("javalin.openapi", "io.javalin.community.openapi:javalin-openapi-plugin:5.4.2")
            library("javalin.swagger", "io.javalin.community.openapi:javalin-swagger-plugin:5.4.2")
            library("micrometer", "io.micrometer:micrometer-core:1.10.5")
            library("micrometer.prometheus", "io.micrometer:micrometer-registry-prometheus:1.10.5")
            library("slf4j", "org.slf4j:slf4j-simple:2.0.5")
            library("dagger", "com.google.dagger:dagger:2.46")
            library("dagger.compiler", "com.google.dagger:dagger-compiler:2.46")
            library("kotlinx.serialization.json", "org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
            library("kotlinx.serialization.protobuf", "org.jetbrains.kotlinx:kotlinx-serialization-protobuf:1.5.0")
            library("kotlinx.coroutines", "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
            library("ksp.processing.api", "com.google.devtools.ksp:symbol-processing-api:1.8.10-1.0.9")
            library("kotlinPoet", "com.squareup:kotlinpoet-ksp:1.12.0")
        }

        create("testLibs") {
            library("jupiter.api", "org.junit.jupiter:junit-jupiter-api:5.9.2")
            library("jupiter.engine", "org.junit.jupiter:junit-jupiter-engine:5.9.2")
        }
    }
}




