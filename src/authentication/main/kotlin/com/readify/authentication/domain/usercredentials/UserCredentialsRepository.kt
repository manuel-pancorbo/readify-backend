package com.readify.authentication.domain.usercredentials

interface UserCredentialsRepository {
    fun save(userCredentials: UserCredentials)
}