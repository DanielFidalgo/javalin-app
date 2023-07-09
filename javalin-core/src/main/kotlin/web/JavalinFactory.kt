package web

import io.javalin.Javalin
import io.javalin.config.JavalinConfig
import io.javalin.openapi.plugin.OpenApiPlugin
import io.javalin.openapi.plugin.OpenApiPluginConfiguration
import io.javalin.openapi.plugin.swagger.SwaggerConfiguration
import io.javalin.openapi.plugin.swagger.SwaggerPlugin
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Tag
import io.micrometer.core.instrument.Tags
import io.micrometer.core.instrument.binder.jetty.InstrumentedQueuedThreadPool
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics
import io.micrometer.core.instrument.binder.system.DiskSpaceMetrics
import io.micrometer.core.instrument.binder.system.ProcessorMetrics
import io.micrometer.core.instrument.binder.system.UptimeMetrics
import org.eclipse.jetty.server.Server
import java.io.File

object JavalinFactory {
    fun create(setup: JavalinSetup, meterRegistry: MeterRegistry, env: String): Javalin {
        val tags = Tags.of("service", setup.name, "env", env)
        bindRegistry(meterRegistry, tags)
        val javalin = Javalin.create(configureJavalin(meterRegistry, setup, env))
        javalin.start(setup.port)
        return javalin
    }

    private fun bindRegistry(registry: MeterRegistry, tags: Tags) {
        ClassLoaderMetrics(tags).bindTo(registry)
        JvmMemoryMetrics(tags).bindTo(registry)
        JvmGcMetrics(tags).bindTo(registry)
        JvmThreadMetrics(tags).bindTo(registry)
        UptimeMetrics(tags).bindTo(registry)
        ProcessorMetrics(tags).bindTo(registry)
        DiskSpaceMetrics(File(System.getProperty("user.dir")), tags).bindTo(registry)
    }

    private fun configureJavalin(meterRegistry: MeterRegistry, setup: JavalinSetup, env: String): (t: JavalinConfig) -> Unit = {
        it.showJavalinBanner = false
        it.http.asyncTimeout = 0
        it.plugins.enableCors { cors ->
            cors.add { config ->
                config.reflectClientOrigin = true
                config.allowCredentials = true
            }
        }
        it.jetty.server {
            Server(InstrumentedQueuedThreadPool(meterRegistry, listOf(Tag.of("server", "Jetty")), setup.maxThreads, setup.minThreads))
        }
        if (env.contains("prod")
                .not()) {
            setupOpenApi(it, setup)
        }
        setup.spaPath?.run {
            it.spaRoot.addFile("/", this)
        }
        setup.customJsonMapper?.run(it::jsonMapper)
    }

    private fun setupOpenApi(config: JavalinConfig, setup: JavalinSetup) {
        val docsPath = "/swagger-docs"
        config.plugins.register(OpenApiPlugin(OpenApiPluginConfiguration(setup.swaggerDocs ?: docsPath) { _, definition ->
            definition.withOpenApiInfo { info ->
                info.title = setup.name
                info.version = setup.version
            }
        }))

        val swagger = SwaggerConfiguration()
        swagger.documentationPath = docsPath
        config.plugins.register(SwaggerPlugin(swagger))
    }
}