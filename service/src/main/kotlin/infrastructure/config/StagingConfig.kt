package infrastructure.config

import infrastructure.config.common.AppConfig
import infrastructure.config.common.Env
import sql.JdbcConfig

class StagingConfig: AppConfig() {
    override fun env(): Env {
        return Env.STAGING
    }

    override fun jdbcConfig(): JdbcConfig {
        TODO("Not yet implemented")
    }
}