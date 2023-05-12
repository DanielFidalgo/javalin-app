package sql.datasources

import io.agroal.api.AgroalDataSource
import io.agroal.api.configuration.AgroalConnectionFactoryConfiguration.TransactionIsolation.SERIALIZABLE
import io.agroal.api.configuration.AgroalConnectionPoolConfiguration.ConnectionValidator.defaultValidator
import io.agroal.api.configuration.AgroalDataSourceConfiguration.DataSourceImplementation.AGROAL
import io.agroal.api.configuration.supplier.AgroalConnectionFactoryConfigurationSupplier
import io.agroal.api.configuration.supplier.AgroalConnectionPoolConfigurationSupplier
import io.agroal.api.configuration.supplier.AgroalDataSourceConfigurationSupplier
import io.agroal.api.security.NamePrincipal
import io.agroal.api.security.SimplePassword
import sql.JdbcConfig
import java.time.Duration.ofSeconds
import javax.sql.DataSource


class AgroalDatasourceFactory: DatasourceFactory {
    override fun create(jdbcConfig: JdbcConfig): DataSource {
        val configuration = AgroalDataSourceConfigurationSupplier()
            .dataSourceImplementation(AGROAL)
            .metricsEnabled(false)
            .connectionPoolConfiguration { cp ->
                cp.minSize(jdbcConfig.minPool)
                    .maxSize(jdbcConfig.maxPool)
                    .initialSize(jdbcConfig.minPool)
                    .connectionValidator(defaultValidator())
                    .acquisitionTimeout(ofSeconds(30))
                    .leakTimeout(ofSeconds(5))
                    .validationTimeout(ofSeconds(50))
                    .reapTimeout(ofSeconds(500))
                    .connectionFactoryConfiguration { cf ->
                        setConnectionConfig(jdbcConfig, cf)
                    }
            }

        val dataSource = AgroalDataSource.from(configuration)

        Runtime.getRuntime().addShutdownHook(Thread { dataSource.close() })

        return dataSource!!
    }

    private fun setConnectionConfig(jdbcConfig: JdbcConfig,
                                    cf: AgroalConnectionFactoryConfigurationSupplier):
            AgroalConnectionFactoryConfigurationSupplier {
        cf.jdbcUrl(jdbcConfig.connectionString)
        cf.connectionProviderClassName(jdbcConfig.driverClass)
        cf.autoCommit(true)
        cf.principal(NamePrincipal(jdbcConfig.user))
        cf.credential(SimplePassword(jdbcConfig.password))
        cf.jdbcTransactionIsolation(SERIALIZABLE)
        return cf
    }
}