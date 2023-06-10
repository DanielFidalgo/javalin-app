package infrastructure.cdi

import dagger.Module
import dagger.Provides
import domain.users.UserRepository
import infrastructure.sql.JooqUserRepository
import javax.inject.Singleton

@Module
class DomainModule {

    @Provides
    @Singleton
    fun providesUserRepository(jooqUserRepository: JooqUserRepository) : UserRepository {
        return jooqUserRepository
    }
}