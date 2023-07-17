package infrastructure.config

import com.github.fidalgotech.JdbcConfig
import infrastructure.config.common.AppConfig
import infrastructure.config.common.Env

class BetaConfig: AppConfig() {
    override fun env(): Env {
        return Env.BETA
    }

    override fun jdbcConfig(): JdbcConfig {
        TODO("Not yet implemented")
    }
}