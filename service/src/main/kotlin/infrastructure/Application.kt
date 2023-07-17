package infrastructure

import com.github.fidalgotech.flyway.FlywayMigrate
import infrastructure.cdi.DaggerAppComponent
import web.Resource
import javax.inject.Inject
import javax.sql.DataSource

class Application {
    @Inject
    lateinit var javalinApi: JavalinApi
    @Inject
    lateinit var dataSource: DataSource
    @Inject
    lateinit var resources: ArrayList<Resource>
    init {
        DaggerAppComponent.create().inject(this)
        FlywayMigrate.migrate(dataSource)
    }
}
