package com.readify.userprofile.domain.user

interface UserRepository {
    fun save(user: User)
    fun findByUsername(username: String): User?
    fun findByEmail(email: String): User?
    fun findById(userId: UserId): User?
    fun findByIds(users: List<UserId>): List<User>
}
