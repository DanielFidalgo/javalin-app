package infrastructure.cdi

import dagger.Component
import infrastructure.Application
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(application: Application)
}