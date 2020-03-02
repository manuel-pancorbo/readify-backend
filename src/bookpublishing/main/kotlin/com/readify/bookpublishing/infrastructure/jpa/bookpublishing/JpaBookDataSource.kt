package com.readify.bookpublishing.infrastructure.jpa.bookpublishing

import org.springframework.data.jpa.repository.JpaRepository
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

interface JpaBookDataSource: JpaRepository<JpaBook, String>

@Entity
@Table(name = "book", schema = "bookpublishing")
data class JpaBook(
    @Id
    val id: String,
    val authorId: String,
    val title: String,
    val cover: String,
    val summary: String
)