package infrastructure.config.common

import com.github.fidalgotech.JdbcConfig
import org.jooq.SQLDialect

abstract class AppConfig {
    private val port = System.getenv(APP_PORT)?.toInt() ?: defaultAppPort

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
        return defaultMinThreads
    }

    fun maxThreads(): Int {
        return defaultMaxThreads
    }

    fun sqlDialect(): SQLDialect {
        return SQLDialect.H2
    }

    abstract fun jdbcConfig(): JdbcConfig

    companion object {
        const val APP_NAME = "Javalin service"
        const val VERSION = "1.0"
        const val defaultMinPool = 10
        const val defaultMaxPool = 100
        const val defaultAcquireIncrement = 5
        const val defaultIdleTestDuration = 10L
        const val defaultMinThreads = 10
        const val defaultMaxThreads = 200
        const val APP_PORT = "APP_PORT"
        const val defaultAppPort = 8080
    }
}