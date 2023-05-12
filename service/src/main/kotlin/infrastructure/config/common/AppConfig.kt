package infrastructure.config.common

import sql.JdbcConfig
import javax.sql.DataSource

abstract class AppConfig {
    private val port = System.getenv("APP_PORT")?.toInt() ?: 8080

    abstract fun env(): Env

    fun name(): String {
        return APP_NAME
    }

    fun version(): String {
        return VERSION
    }

    fun port(): Int {
        return port
    }

    fun minThreads(): Int {
        return 10
    }

    fun maxThreads(): Int {
        return 200
    }

    abstract fun datasource(): JdbcConfig

    companion object {
        private const val APP_NAME = "Javalin service"
        private const val VERSION = "1.0"
    }
}