package infrastructure.config

import com.github.fidalgotech.JdbcConfig
import infrastructure.config.common.AppConfig
import infrastructure.config.common.Env
import java.time.Duration

class StubConfig: AppConfig() {
    override fun env(): Env {
        return Env.STUB
    }

    override fun jdbcConfig(): JdbcConfig {
        return JdbcConfig(
            "org.h2.Driver",
            "jdbc:h2:mem:jooq;INIT=CREATE SCHEMA IF NOT EXISTS jooq\\;SET SCHEMA jooq;MODE=MySQL;",
            "sa",
            "",
            false,
            defaultMinPool,
            defaultMaxPool,
            defaultAcquireIncrement,
            Duration.ofSeconds(defaultIdleTestDuration))
    }
}