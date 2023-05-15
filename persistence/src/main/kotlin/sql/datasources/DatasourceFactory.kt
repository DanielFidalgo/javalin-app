package sql.datasources

import sql.JdbcConfig
import javax.sql.DataSource

interface DatasourceFactory {
    fun create(jdbcConfig: JdbcConfig): DataSource
    companion object {
        val AGROAL = AgroalDatasourceFactory()
    }
}