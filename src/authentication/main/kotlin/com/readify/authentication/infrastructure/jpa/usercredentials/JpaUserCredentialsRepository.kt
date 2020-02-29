package com.readify.authentication.infrastructure.jpa.usercredentials

import com.readify.authentication.domain.usercredentials.EncodedPassword
import com.readify.authentication.domain.usercredentials.UserCredentials
import com.readify.authentication.domain.usercredentials.UserCredentialsRepository
import com.readify.authentication.domain.usercredentials.UserIdentifier

class JpaUserCredentialsRepository(private val jpaUserCredentialsDataSource: JpaUserCredentialsDataSource) :
    UserCredentialsRepository {
    override fun save(userCredentials: UserCredentials) {
        jpaUserCredentialsDataSource.save(userCredentials.toJpa())
    }

    override fun findByUserIdentifierAndPassword(
        userIdentifier: UserIdentifier,
        password: EncodedPassword
    ): UserCredentials? {
        return null
    }
}

private fun UserCredentials.toJpa() =
    JpaUserCredentials(this.userId.value, this.username.value, this.email.value, this.encodedPassword.value)
