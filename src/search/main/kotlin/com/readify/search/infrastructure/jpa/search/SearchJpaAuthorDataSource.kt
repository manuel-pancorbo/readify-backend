package com.readify.search.infrastructure.jpa.search

import org.springframework.data.jpa.repository.JpaRepository
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

interface SearchJpaAuthorDataSource : JpaRepository<JpaAuthor, String>

@Entity
@Table(name = "author", schema = "search")
data class JpaAuthor(
    @Id
    val id: String,
    val fullName: String,
    val username: String
)
