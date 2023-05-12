package sql.flyway

import org.flywaydb.core.Flyway
import org.flywaydb.core.api.FlywayException
import java.util.logging.Level
import java.util.logging.Logger
import javax.sql.DataSource

class FlywayMigrate {
    companion object {
        private const val RETRY_ATTEMPTS = 11

        fun migrate(dataSource: DataSource?) {
            migrate(dataSource, false)
        }

        fun migrate(dataSource: DataSource?, testMode: Boolean) {
            val fluentConfiguration = Flyway.configure()
            if (testMode) {
                if (System.getProperty("testData", "true") == "true") {
                    fluentConfiguration.locations("db/migration", "db/test")
                }
            }
            if( isRunningInNativeImage() ) {
                fluentConfiguration.resourceProvider(GraalVMResourceProvider(fluentConfiguration.locations))
            }
            fluentConfiguration.dataSource(dataSource)
            val flyway = fluentConfiguration.load()
            var i = 0
            while (i < RETRY_ATTEMPTS) {
                try {
                    flyway.migrate()
                    break
                } catch (e: FlywayException) {
                    if (e.message?.contains("lock table")!! || e.message?.contains("schema_version")!!) {
                        throw e
                    }
                    ++i
                    if (i == RETRY_ATTEMPTS) {
                        Logger.getAnonymousLogger().log(Level.SEVERE, "Unable to apply migration", e)
                        throw e
                    }
                }
            }
        }
        private fun isRunningInNativeImage(): Boolean {
            return System.getProperty("org.graalvm.nativeimage.imagecode") != null
        }
    }
}