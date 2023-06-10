package infrastructure.sql.serializers

import domain.users.User
import infrastructure.tables.pojos.Users

object UserSerializer {

    fun serializeUserPojo(domain: User): Users {
        return Users(domain.id, domain.name, domain.email, domain.created)
    }
    fun deserializeUserPojo(user: Users) : User {
        return User(user.id!!, user.name!!, user.email!!, user.created!!)
    }
}