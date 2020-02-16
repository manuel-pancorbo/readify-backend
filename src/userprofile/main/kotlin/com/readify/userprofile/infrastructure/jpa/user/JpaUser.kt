package com.readify.userprofile.infrastructure.jpa.user

import com.readify.userprofile.domain.user.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "userprofile", schema = "userprofiles")
data class JpaUser (@Id val id: String, val username: String, val email: String, val password: String) {
    fun toDomain() = User(UserId(id), Username(username), Email(email), Password(password))
}