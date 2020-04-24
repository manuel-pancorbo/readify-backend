package com.readify.userprofile.infrastructure.jpa.user

import com.readify.userprofile.domain.user.User
import com.readify.userprofile.domain.user.UserId
import com.readify.userprofile.domain.user.UserRepository

class JpaUserRepository(private val jpaJpaUserDataSource: JpaUserDataSource): UserRepository {
    override fun save(user: User) {
        jpaJpaUserDataSource.save(user.toJpa())
    }

    override fun findByUsername(username: String): User? =
        jpaJpaUserDataSource.findByUsername(username)?.toDomain()

    override fun findByEmail(email: String): User? =
        jpaJpaUserDataSource.findByEmail(email)?.toDomain()

    override fun findById(userId: UserId): User? =
        jpaJpaUserDataSource.findById(userId.value).map { it.toDomain() }.orElse(null)
}

private fun User.toJpa() = JpaUser(id.value, username.value, email.value, fullName.value, image?.value, website?.value)
