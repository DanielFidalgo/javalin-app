package api

import domain.users.UserService
import javax.inject.Inject

class UserApi @Inject constructor(private val userService: UserService) {

    fun getUser(id: String) {
        userService.getUser(id)
    }
}