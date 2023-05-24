package infrastructure.resources

import api.UserApi
import io.javalin.Javalin
import web.Resource
import io.javalin.http.Handler
import io.micrometer.core.instrument.MeterRegistry
import javax.inject.Inject

class ItemResource @Inject constructor(javalin: Javalin,
                                       registry: MeterRegistry,
                                       itemApi: UserApi) : Resource(javalin, registry) {
    override fun configure() {
        timedGet("timed.get", "/timed/app", getItem())
    }

    private fun getItem() = Handler {
        it.result("timed")
    }
}