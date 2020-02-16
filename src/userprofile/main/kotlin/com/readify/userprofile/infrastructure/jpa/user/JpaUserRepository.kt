package com.readify.userprofile.infrastructure.jpa.user

import com.readify.userprofile.domain.user.*

class JpaUserRepository(private val jpaJpaUserDataSource: JpaUserDataSource): UserRepository {
    override fun save(user: User): User {
        jpaJpaUserDataSource.save(user.toJpa())
        return user
    }

    override fun findByUsername(username: String): User? =
        jpaJpaUserDataSource.findByUsername(username)?.toDomain()

    override fun findByEmail(email: String): User? =
        jpaJpaUserDataSource.findByEmail(email)?.toDomain()
}

private fun User.toJpa() = JpaUser(id.value, username.value, email.value, password.value)
