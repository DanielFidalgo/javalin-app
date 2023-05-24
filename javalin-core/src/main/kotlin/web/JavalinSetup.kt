package web

import io.javalin.json.JsonMapper

data class JavalinSetup(val port: Int = 7000,
                        val minThreads: Int = 10,
                        val maxThreads: Int = 200,
                        val customJsonMapper: JsonMapper? = null,
                        val name: String,
                        val tag: String? = null,
                        val version: String? = null,
                        val swaggerDocs: String? = null,
                        val swaggerUI: String? = null,
                        val spaPath: String? = null,
                        val isProduction: Boolean = false)
