import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask

plugins {
    kotlin("jvm") version "1.8.20"
    kotlin("kapt")
    kotlin("plugin.serialization") version "1.6.10"
    id("org.graalvm.buildtools.native") version "0.9.21"
    id("com.google.cloud.tools.jib") version "3.3.1"
    id("com.google.devtools.ksp") version "1.8.20-1.0.11"
    id("org.flywaydb.flyway") version "9.16.0"
    id("nu.studer.jooq") version "8.2"
    id("io.gitlab.arturbosch.detekt") version "1.23.0"
    application
    checkstyle
}

group = "io.github.danielfidalgo"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    api(libs.dagger)
    api(libs.kotlinx.serialization.json)
    api(libs.kotlinx.serialization.protobuf)
    api(libs.kotlinx.coroutines)
    api(libs.micrometer.prometheus)
    implementation(project(":javalin-core"))
    implementation(project(":persistence"))
    ksp(project(":javalin-ksp"))
    kapt(libs.dagger.compiler)
    kapt(libs.javalin.openapi.processor)
    implementation("com.h2database:h2:2.1.214")
    jooqGenerator("com.h2database:h2:2.1.214")
    testImplementation(testLibs.jupiter.api)
    testRuntimeOnly(testLibs.jupiter.engine)
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

detekt {
    buildUponDefaultConfig = true // preconfigure defaults
    allRules = false // activate all available (even unstable) rules.
    config.setFrom("$projectDir/detekt.yml") // point to your custom config defining rules to run, overwriting default behavior
    //baseline = file("$projectDir/config/baseline.xml") // a way of suppressing issues before introducing detekt
}

tasks.withType<Detekt>().configureEach {
    reports {
        html.required.set(true) // observe findings in your browser with structure and code snippets
        xml.required.set(true) // checkstyle like format mainly for integrations like Jenkins
        txt.required.set(true) // similar to the console output, contains issue signature to manually edit baseline files
        sarif.required.set(true) // standardized SARIF format (https://sarifweb.azurewebsites.net/) to support integrations with GitHub Code Scanning
        md.required.set(true) // simple Markdown format
    }
}

tasks.withType<Detekt>().configureEach {
    jvmTarget = "17"
}
tasks.withType<DetektCreateBaselineTask>().configureEach {
    jvmTarget = "17"
}

graalvmNative {

    binaries {
        named("main") {
            javaLauncher.set(javaToolchains.launcherFor {
                languageVersion.set(JavaLanguageVersion.of(17))
                vendor.set(JvmVendorSpec.matching("GraalVM Community"))
            })
            mainClass.set("MainKt")
            fallback.set(false)
            buildArgs.add("--report-unsupported-elements-at-runtime")
            buildArgs.add("-H:+ReportExceptionStackTraces")
            configurationFileDirectories.setFrom("src/main/resources/META-INF/native-config")
            resources.autodetect()
        }
    }
    metadataRepository.enabled.set(true)
}

jib {
    from{
        image = "istio/distroless:latest"
    }
    to {
        image = "local/${rootProject.name}:latest"
    }

    pluginExtensions{
        pluginExtension {
            implementation = "com.google.cloud.tools.jib.gradle.extension.nativeimage.JibNativeImageExtension"
            properties = mapOf("imageName" to "${rootProject.name}")
        }
    }
}

ksp {
    arg("javalin.processor.generated.package", "infrastructure")
    arg("javalin.processor.generated.class", "JavalinApi")
    arg("javalin.processor.ignore.type", "Resource")
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("MainKt")
    if (project.hasProperty("graalvmAgent")) {
        applicationDefaultJvmArgs = listOf("-agentlib:native-image-agent=config-merge-dir=src/main/resources/META-INF/native-config")
    }
}

val h2Url by extra("jdbc:h2:file:${buildDir}/jooq;INIT=CREATE SCHEMA IF NOT EXISTS jooq\\;SET SCHEMA jooq;MODE=MySQL;")
val h2User by extra("sa")
val h2Password by extra("")
flyway {
    url = h2Url
    user = h2User
    password = h2Password
}

jooq {
    version.set(libs.jooq.get().versionConstraint.requiredVersion)
    configurations {
        create("main") {
            generateSchemaSourceOnCompilation.set(true)
            jooqConfiguration.apply {
                logging = org.jooq.meta.jaxb.Logging.WARN
                jdbc.apply {
                    url = h2Url
                    user = h2User
                    password = h2Password
                    properties.add(org.jooq.meta.jaxb.Property().apply {
                        key = "ssl"
                        value = "true"
                    })
                }
                generator.apply {
                    name = "org.jooq.codegen.KotlinGenerator"
                    database.apply {
                        name = "org.jooq.meta.h2.H2Database"
                        inputSchema = "JOOQ"
                    }
                    generate.apply {
                        isDeprecated = false
                        isPojosAsKotlinDataClasses = true
                        isFluentSetters = true
                        isDaos = true
                        isImplicitJoinPathsAsKotlinProperties = true
                        isKotlinSetterJvmNameAnnotationsOnIsPrefix = true
                        isKotlinNotNullPojoAttributes = false
                        isKotlinNotNullRecordAttributes = true
                        isKotlinNotNullInterfaceAttributes = false
                    }
                    target.apply {
                        packageName = "infrastructure"
                        directory = "${buildDir}/generated/main/kotlin/jooq/"
                    }
                    strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
                }
            }
        }
    }
}

tasks.getByName("generateJooq").dependsOn(tasks.getByName("flywayMigrate"))




