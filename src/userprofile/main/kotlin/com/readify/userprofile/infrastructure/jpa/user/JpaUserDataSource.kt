package com.readify.userprofile.infrastructure.jpa.user

import org.springframework.data.jpa.repository.JpaRepository

interface JpaUserDataSource : JpaRepository<JpaUser, String> {
    fun findByUsername(username: String): JpaUser?
    fun findByEmail(email: String): JpaUser?
}