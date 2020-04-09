package com.readify.readerlibrary.infrastructure.jpa.readerlibrary

import org.springframework.data.jpa.repository.JpaRepository
import java.time.Instant
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

interface ReaderLibraryJpaChapterDataSource : JpaRepository<JpaChapter, String>

@Entity
@Table(name = "chapter", schema = "readerlibrary")
data class JpaChapter(
    @Id
    val id: String,
    val authorId: String,
    val bookId: String,
    val title: String,
    val content: String,
    val modifiedAt: Instant,
    val publishedAt: Instant?,
    val priceAmount: Float,
    val priceCurrency: String,
    val chapterOrder: Int,
    val excerpt: String?
)