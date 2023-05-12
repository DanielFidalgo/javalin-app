package infrastructure.config

import infrastructure.config.common.AppConfig
import infrastructure.config.common.Env
import sql.JdbcConfig
import javax.sql.DataSource

class BetaConfig: AppConfig() {
    override fun env(): Env {
        return Env.BETA
    }

    override fun datasource(): JdbcConfig {
        TODO("Not yet implemented")
    }
}