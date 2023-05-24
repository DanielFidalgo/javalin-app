package infrastructure.sql

import domain.UserRepository
import sql.jooq.DslContext
import javax.inject.Inject

class JooqUserRepository @Inject constructor(private val context: DslContext) : UserRepository {

}