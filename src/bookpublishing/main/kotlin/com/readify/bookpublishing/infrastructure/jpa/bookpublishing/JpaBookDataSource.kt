package com.readify.bookpublishing.infrastructure.jpa.bookpublishing

import com.vladmihalcea.hibernate.type.array.ListArrayType
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import org.springframework.data.jpa.repository.JpaRepository
import java.time.Instant
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id
import javax.persistence.Table

interface JpaBookDataSource : JpaRepository<JpaBook, String>

@Entity
@Table(name = "book", schema = "bookpublishing")
@TypeDef(name = "list-array", typeClass = ListArrayType::class)
data class JpaBook(
    @Id
    val id: String,
    val authorId: String,
    val title: String,
    val cover: String,
    val summary: String,
    @Type(type = "list-array")
    val tags: List<String>,
    val priceAmount: Float,
    val priceCurrency: String,
    @Enumerated(EnumType.STRING)
    val status: JpaBookStatus,
    val completionPercentage: Int,
    @Enumerated(EnumType.STRING)
    val visibility: JpaBookVisibility,
    val finishedAt: Instant?
)

enum class JpaBookStatus { IN_PROGRESS, FINISHED }
enum class JpaBookVisibility { NULL, RESTRICTED, VISIBLE }