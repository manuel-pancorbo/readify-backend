package com.readify.userprofile.domain.user

interface UserRepository {
    fun save(user: User)
    fun findByUsername(username: String): User?
    fun findByEmail(email: String): User?
}
