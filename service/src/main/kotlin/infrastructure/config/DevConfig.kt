package infrastructure.config

import infrastructure.config.common.AppConfig
import infrastructure.config.common.Env
import sql.JdbcConfig

class DevConfig: AppConfig() {
    override fun env(): Env {
     return Env.DEV
    }

    override fun jdbcConfig(): JdbcConfig {
        TODO("Not yet implemented")
    }
}