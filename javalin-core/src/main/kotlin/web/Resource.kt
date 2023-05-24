package web

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder
import io.javalin.apibuilder.EndpointGroup
import io.javalin.http.Context
import io.javalin.http.ExceptionHandler
import io.javalin.http.Handler
import io.javalin.http.HttpStatus
import io.micrometer.core.instrument.MeterRegistry
import java.lang.RuntimeException

abstract class Resource(val javalin: Javalin, private val registry: MeterRegistry? = null) {

    init {
        this.configure()
    }

    abstract fun configure()

    open fun get(path: String, handler: Handler) {
        javalin.get(path, handler)
    }

    open fun get(handler: Handler) {
        ApiBuilder.get(handler)
    }

    open fun timedGet(name: String, path: String, handler: Handler) {
        javalin.get(path) {
            timedHandler(name, handler, it)
        }
    }

    open fun timedGet(name: String, handler: Handler) {
        ApiBuilder.get {
            timedHandler(name, handler, it)
        }
    }

    open fun post(path: String, handler: Handler) {
        javalin.post(path, handler)
    }

    open fun post(handler: Handler) {
        ApiBuilder.post(handler)
    }

    open fun timedPost(name: String, path: String, handler: Handler) {
        javalin.post(path) {
            timedHandler(name, handler, it)
        }
    }

    open fun timedPost(name: String, handler: Handler) {
        ApiBuilder.post {
            timedHandler(name, handler, it)
        }
    }

    open fun patch(path: String, handler: Handler) {
        javalin.patch(path, handler)
    }

    open fun patch(handler: Handler) {
        ApiBuilder.patch(handler)
    }

    open fun delete(path: String, handler: Handler) {
        javalin.delete(path, handler)
    }

    open fun delete(handler: Handler) {
        ApiBuilder.delete(handler)
    }

    open fun put(path: String, handler: Handler) {
        javalin.put(path, handler)
    }

    open fun put(handler: Handler) {
        ApiBuilder.put(handler)
    }

    open fun routes(endpointGroup: EndpointGroup): Javalin {
        return javalin.routes(endpointGroup)
    }

    open fun before(path: String, handler: Handler): Javalin {
        return javalin.before(path, handler)
    }

    open fun before(handler: Handler): Javalin {
        return javalin.before(handler)
    }

    open fun after(path: String, handler: Handler): Javalin {
        return javalin.after(path, handler)
    }

    open fun after(handler: Handler): Javalin {
        return javalin.after(handler)
    }

    open fun error(status: Int, handler: Handler): Javalin {
        return javalin.error(status, handler)
    }

    open fun error(status: Int, contentType: String, handler: Handler): Javalin {
        return javalin.error(status, contentType, handler)
    }

    open fun options(path: String, handler: Handler): Javalin {
        return javalin.options(path, handler)
    }

    inline fun <reified T : Exception> exception(exceptionClass: Class<T>, exceptionHandler: ExceptionHandler<T>): Javalin {
        return javalin.exception(exceptionClass, exceptionHandler)
    }

    private fun getRegistry(): MeterRegistry {
        return registry ?: throw RuntimeException(registryNotFound)
    }

    private fun timedHandler(name: String, handler: Handler, context: Context) {
        getRegistry().timer(name).recordCallable { handler.handle(context) }
    }

    companion object {
        private const val registryNotFound = "Meter Registry Not Found"
        fun proxy(apiRedirect: String) = Handler {
            val location = "${apiRedirect}${it.path()}${it.queryString()?.let { q -> "?${q}" }}"
            it.redirect(location, HttpStatus.TEMPORARY_REDIRECT)
        }
    }
}