package infrastructure.config

import infrastructure.config.common.AppConfig
import infrastructure.config.common.Env
import sql.JdbcConfig

class BetaConfig: AppConfig() {
    override fun env(): Env {
        return Env.BETA
    }

    override fun jdbcConfig(): JdbcConfig {
        TODO("Not yet implemented")
    }
}