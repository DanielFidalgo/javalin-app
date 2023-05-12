package sql

import sql.datasources.DatasourceFactory
import java.time.Duration

data class JdbcConfig(val driverClass: String,
                      val connectionString: String,
                      val user: String,
                      val password: String,
                      val testMode: Boolean,
                      val minPool: Int,
                      val maxPool: Int,
                      val acquireIncrement: Int,
                      val idleConnectionTestPeriod: Duration,
                      val datasourceFactory: DatasourceFactory)
