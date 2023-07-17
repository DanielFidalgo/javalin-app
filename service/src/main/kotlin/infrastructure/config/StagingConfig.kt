package infrastructure.config

import com.github.fidalgotech.JdbcConfig
import infrastructure.config.common.AppConfig
import infrastructure.config.common.Env

class StagingConfig: AppConfig() {
    override fun env(): Env {
        return Env.STAGING
    }

    override fun jdbcConfig(): JdbcConfig {
        TODO("Not yet implemented")
    }
}