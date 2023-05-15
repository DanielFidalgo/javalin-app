package infrastructure.config

import infrastructure.config.common.AppConfig
import infrastructure.config.common.Env
import sql.JdbcConfig

class ProdConfig: AppConfig() {
    override fun env(): Env {
        return Env.PROD
    }

    override fun jdbcConfig(): JdbcConfig {
        TODO("Not yet implemented")
    }
}