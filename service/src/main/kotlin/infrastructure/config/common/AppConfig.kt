package infrastructure.config.common

import org.jooq.SQLDialect
import sql.JdbcConfig

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

    fun sqlDialect(): SQLDialect {
        return SQLDialect.H2
    }

    abstract fun jdbcConfig(): JdbcConfig

    companion object {
        private const val APP_NAME = "Javalin service"
        private const val VERSION = "1.0"
    }
}