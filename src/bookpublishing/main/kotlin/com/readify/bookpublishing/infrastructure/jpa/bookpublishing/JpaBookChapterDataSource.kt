package com.readify.bookpublishing.infrastructure.jpa.bookpublishing

import org.springframework.data.jpa.repository.JpaRepository
import java.time.Instant
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id
import javax.persistence.Table

interface JpaChapterDataSource : JpaRepository<JpaChapter, String> {
    fun findByIdAndBookId(id: String, bookId: String): JpaChapter?
}

@Entity
@Table(name = "chapter", schema = "bookpublishing")
data class JpaChapter(
    @Id
    val id: String,
    val authorId: String,
    val bookId: String,
    val title: String,
    val content: String,
    val modifiedAt: Instant,
    val publishedAt: Instant?,
    @Enumerated(EnumType.STRING)
    val status: JpaChapterStatus,
    val priceAmount: Float,
    val priceCurrency: String
)

enum class JpaChapterStatus { DRAFT, PUBLISHED }