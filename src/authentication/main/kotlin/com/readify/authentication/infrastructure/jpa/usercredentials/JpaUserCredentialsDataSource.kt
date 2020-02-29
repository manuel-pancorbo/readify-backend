package com.readify.authentication.infrastructure.jpa.usercredentials

import org.springframework.data.jpa.repository.JpaRepository

interface JpaUserCredentialsDataSource : JpaRepository<JpaUserCredentials, String> {
    fun findByUsernameOrEmail(username: String, email: String): JpaUserCredentials?
}
