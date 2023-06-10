package domain.users

import java.time.LocalDateTime

data class User(val id: String, val name: String, val email: String, val created: LocalDateTime)
