package infrastructure.config

import infrastructure.config.common.AppConfig
import infrastructure.config.common.Env
import sql.JdbcConfig
import javax.sql.DataSource

class DevConfig: AppConfig() {
    override fun env(): Env {
     return Env.DEV
    }

    override fun datasource(): JdbcConfig {
        TODO("Not yet implemented")
    }
}