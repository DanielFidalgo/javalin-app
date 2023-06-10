package domain.users

import javax.inject.Inject

class UserService @Inject constructor(private val userRepository: UserRepository) {

    fun getUser(id: String) {
        userRepository.getUser(id)
    }
}