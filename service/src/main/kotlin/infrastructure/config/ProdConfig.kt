package infrastructure.config

import com.github.fidalgotech.JdbcConfig
import infrastructure.config.common.AppConfig
import infrastructure.config.common.Env

class ProdConfig: AppConfig() {
    override fun env(): Env {
        return Env.PROD
    }

    override fun jdbcConfig(): JdbcConfig {
        TODO("Not yet implemented")
    }
}