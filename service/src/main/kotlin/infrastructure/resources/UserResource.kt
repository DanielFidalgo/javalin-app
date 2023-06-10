package infrastructure.resources

import api.UserApi
import io.javalin.Javalin
import web.Resource
import io.javalin.http.Handler
import io.javalin.openapi.OpenApi
import io.javalin.openapi.OpenApiParam
import io.micrometer.core.instrument.MeterRegistry
import javax.inject.Inject

class UserResource @Inject constructor(javalin: Javalin,
                                       registry: MeterRegistry,
                                       private val userApi: UserApi) : Resource(javalin, registry) {
    override fun configure() {
        routes {
            path("/user") {
                path("/{id}"){
                    timedGet("timed.get", getUser())
                }
            }
        }

    }

    @OpenApi(path = "/user/{id}",
        methods = [io.javalin.openapi.HttpMethod.GET],
        pathParams = [OpenApiParam(name = "id", required = true)])
    private fun getUser() = Handler {
        val id = it.pathParam("id")
        userApi.getUser(id)
    }
}