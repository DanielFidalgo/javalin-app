package infrastructure.resources

import io.javalin.http.Handler
import io.javalin.http.HttpStatus
import io.javalin.openapi.HttpMethod
import io.javalin.openapi.OpenApi
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.composite.CompositeMeterRegistry
import io.micrometer.prometheus.PrometheusMeterRegistry
import javax.inject.Inject


class RootResource @Inject constructor(private val meterRegistry: MeterRegistry) {
    @OpenApi(path="/health-check", methods = [HttpMethod.GET])
    fun healthCheck() = Handler {
        val registry = (meterRegistry as CompositeMeterRegistry).registries
            .first(PrometheusMeterRegistry::class::isInstance) as PrometheusMeterRegistry
        it.result(registry.scrape())
        it.status(HttpStatus.OK)
    }

    @OpenApi(path="/ping", methods = [HttpMethod.GET])
    fun ping() = Handler {
        it.result("pong")
        it.status(HttpStatus.OK)
    }
}