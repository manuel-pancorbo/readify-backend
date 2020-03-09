package com.readify.bookpublishing.infrastructure.jpa.bookpublishing

import org.springframework.data.jpa.repository.JpaRepository
import java.time.ZonedDateTime
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id
import javax.persistence.Table

interface JpaChapterDataSource : JpaRepository<JpaChapter, String>

@Entity
@Table(name = "chapter", schema = "bookpublishing")
data class JpaChapter(
    @Id
    val id: String,
    val authorId: String,
    val bookId: String,
    val title: String,
    val content: String,
    val modifiedAt: ZonedDateTime,
    val publishedAt: ZonedDateTime?,
    @Enumerated(EnumType.STRING)
    val status: JpaChapterStatus
)

enum class JpaChapterStatus { DRAFT, PUBLISHED }