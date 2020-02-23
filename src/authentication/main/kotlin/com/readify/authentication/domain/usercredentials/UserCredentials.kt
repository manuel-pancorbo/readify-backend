package com.readify.authentication.domain.usercredentials

import com.readify.shared.domain.event.RootAggregate

data class UserCredentials(
    val userId: UserId,
    val username: Username,
    val email: Email,
    val encryptedPassword: EncryptedPassword
) :
    RootAggregate()

data class UserId(val value: String)
data class Username(val value: String)
data class Email(val value: String)
data class EncryptedPassword(val value: String)
