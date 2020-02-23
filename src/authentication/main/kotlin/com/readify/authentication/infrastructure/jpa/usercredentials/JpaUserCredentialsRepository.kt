package com.readify.authentication.infrastructure.jpa.usercredentials

import com.readify.authentication.domain.usercredentials.UserCredentials
import com.readify.authentication.domain.usercredentials.UserCredentialsRepository

class JpaUserCredentialsRepository(private val jpaUserCredentialsDataSource: JpaUserCredentialsDataSource) :
    UserCredentialsRepository {
    override fun save(userCredentials: UserCredentials) {
        jpaUserCredentialsDataSource.save(userCredentials.toJpa())
    }
}

private fun UserCredentials.toJpa() =
    JpaUserCredentials(this.userId.value, this.username.value, this.email.value, this.encryptedPassword.value)
