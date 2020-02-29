package com.readify.authentication.infrastructure.jpa.usercredentials

import com.readify.authentication.domain.usercredentials.Email
import com.readify.authentication.domain.usercredentials.EncodedPassword
import com.readify.authentication.domain.usercredentials.UserCredentials
import com.readify.authentication.domain.usercredentials.UserCredentialsRepository
import com.readify.authentication.domain.usercredentials.UserId
import com.readify.authentication.domain.usercredentials.UserIdentifier
import com.readify.authentication.domain.usercredentials.Username

class JpaUserCredentialsRepository(private val jpaUserCredentialsDataSource: JpaUserCredentialsDataSource) :
    UserCredentialsRepository {
    override fun save(userCredentials: UserCredentials) {
        jpaUserCredentialsDataSource.save(userCredentials.toJpa())
    }

    override fun findByUserIdentifier(userIdentifier: UserIdentifier) =
        jpaUserCredentialsDataSource
            .findByUsernameOrEmail(userIdentifier.value, userIdentifier.value)
            ?.toDomain()
}

private fun JpaUserCredentials?.toDomain() =
    this?.let {
        UserCredentials(
            UserId(this.id),
            Username(this.username),
            Email(this.email),
            EncodedPassword(this.password)
        )
    }

private fun UserCredentials.toJpa() =
    JpaUserCredentials(this.userId.value, this.username.value, this.email.value, this.encodedPassword.value)
