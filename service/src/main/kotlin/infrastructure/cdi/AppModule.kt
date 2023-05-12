package infrastructure.cdi

import JavalinSetup
import dagger.Module
import dagger.Provides
import infrastructure.config.BetaConfig
import infrastructure.config.DevConfig
import infrastructure.config.ProdConfig
import infrastructure.config.StagingConfig
import infrastructure.config.StubConfig
import infrastructure.config.common.AppConfig
import infrastructure.config.common.Env
import infrastructure.serialization.CustomJsonMapper
import io.agroal.api.AgroalDataSource
import io.javalin.Javalin
import io.micrometer.core.instrument.Clock
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.composite.CompositeMeterRegistry
import io.micrometer.prometheus.PrometheusConfig
import io.micrometer.prometheus.PrometheusMeterRegistry
import javax.inject.Singleton
import javax.sql.DataSource

@Module
class AppModule {

    @Provides
    @Singleton
    fun providesAppConfig(): AppConfig {
        val env = System.getenv("ENV")
        return when (Env.getByLabel(env)) {
            Env.PROD -> ProdConfig()
            Env.BETA -> BetaConfig()
            Env.STAGING -> StagingConfig()
            Env.DEV -> DevConfig()
            else -> StubConfig()
        }
    }

    @Provides
    @Singleton
    fun providesMeterRegistry(): MeterRegistry {
        val meterRegistry = CompositeMeterRegistry(Clock.SYSTEM)
        meterRegistry.add(PrometheusMeterRegistry(PrometheusConfig.DEFAULT))
        return meterRegistry
    }

    @Provides
    @Singleton
    fun provideJavalin(appConfig: AppConfig, meterRegistry: MeterRegistry): Javalin {
        val setup = JavalinSetup(appConfig.port(),
            appConfig.minThreads(),
            appConfig.maxThreads(),
            CustomJsonMapper(),
            appConfig.name(),
            appConfig.version(),
            isProduction = appConfig.env() == Env.PROD )
        return JavalinFactory.create(setup, meterRegistry, appConfig.env().label)
    }

    @Provides
    @Singleton
    fun providesAgroalDataSource(appConfig: AppConfig): AgroalDataSource {
        val jdbcConfig = appConfig.datasource()
        return jdbcConfig.datasourceFactory.create(jdbcConfig) as AgroalDataSource
    }

    @Provides
    @Singleton
    fun providesDataSource(ds: AgroalDataSource): DataSource {
        return ds
    }
}