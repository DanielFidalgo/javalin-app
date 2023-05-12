package infrastructure.config

import infrastructure.config.common.AppConfig
import infrastructure.config.common.Env
import sql.JdbcConfig
import sql.datasources.AgroalDatasourceFactory
import java.time.Duration

class StubConfig: AppConfig() {

    init {

    }
    override fun env(): Env {
        return Env.STUB
    }

    override fun datasource(): JdbcConfig {
        return JdbcConfig("org.h2.Driver",
            "jdbc:h2:mem:jooq;INIT=CREATE SCHEMA IF NOT EXISTS jooq\\;SET SCHEMA jooq;MODE=MySQL;",
            "sa",
            "",
            false,
            10,
            100,
            5,
            Duration.ofSeconds(10),
            AgroalDatasourceFactory())
    }
}