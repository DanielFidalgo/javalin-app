package domain.users

interface UserRepository {

    fun insert(user: User) {
        insert(setOf(user))
    }

    fun insert(users: Set<User>)
    fun getUser(id: String): User? {
        return getUsers(setOf(id)).firstOrNull()
    }

    fun getUsers(ids: Set<String>): List<User>
}