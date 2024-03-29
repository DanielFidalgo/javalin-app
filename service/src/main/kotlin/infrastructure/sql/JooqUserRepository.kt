package infrastructure.sql

import com.github.fidalgotech.jooq.DslContext
import domain.users.User
import domain.users.UserRepository
import infrastructure.sql.serializers.UserSerializer
import infrastructure.tables.daos.UsersDao
import javax.inject.Inject

class JooqUserRepository @Inject constructor(context: DslContext) : UserRepository {

    private val writerDao: UsersDao = UsersDao(context.writer.configuration())
    private val readerDao: UsersDao = UsersDao(context.reader.configuration())
    override fun insert(users: Set<User>) {
        writerDao.insert(users.map(UserSerializer::serializeUserPojo))
    }

    override fun getUsers(ids: Set<String>): List<User> {
        return readerDao.fetchById(ids.fold("") { acc, next -> "$acc$next" }).map(UserSerializer::deserializeUserPojo)
    }
}