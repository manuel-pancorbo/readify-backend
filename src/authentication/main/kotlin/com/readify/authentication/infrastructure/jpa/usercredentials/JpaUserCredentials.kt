package com.readify.authentication.infrastructure.jpa.usercredentials

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "usercredential", schema = "authentication")
data class JpaUserCredentials(@Id val id: String, val username: String, val email: String, val password: String)