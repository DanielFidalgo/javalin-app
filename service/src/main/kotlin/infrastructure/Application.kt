package infrastructure

import infrastructure.cdi.DaggerAppComponent
import web.Resource
import sql.flyway.FlywayMigrate
import javax.inject.Inject
import javax.sql.DataSource

class Application {
    @Inject
    lateinit var javalinApi: JavalinApi
    @Inject
    lateinit var dataSource: DataSource
    @Inject
    lateinit var resouraaaaa: ArrayList<Resource>
    init {
        DaggerAppComponent.create().inject(this)
        FlywayMigrate.migrate(dataSource)
    }
}