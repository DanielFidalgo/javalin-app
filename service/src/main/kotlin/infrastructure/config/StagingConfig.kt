package infrastructure.config

import infrastructure.config.common.AppConfig
import infrastructure.config.common.Env
import sql.JdbcConfig
import javax.sql.DataSource

class StagingConfig: AppConfig() {
    override fun env(): Env {
        return Env.STAGING
    }

    override fun datasource(): JdbcConfig {
        TODO("Not yet implemented")
    }
}