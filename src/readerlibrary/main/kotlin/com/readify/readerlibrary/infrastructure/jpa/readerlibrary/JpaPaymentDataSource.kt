package com.readify.readerlibrary.infrastructure.jpa.readerlibrary

import com.vladmihalcea.hibernate.type.array.ListArrayType
import org.hibernate.annotations.TypeDef
import org.springframework.data.jpa.repository.JpaRepository
import java.time.Instant
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id
import javax.persistence.Table

interface ReaderLibraryJpaPaymentDataSource : JpaRepository<JpaPayment, String> {
    fun findByStatusAndReaderIdOrderByCompletedAtDesc(status: JpaPaymentStatus, readerId: String): List<JpaPayment>
}

@Entity
@Table(name = "payment", schema = "readerlibrary")
@TypeDef(name = "list-array", typeClass = ListArrayType::class)
data class JpaPayment(
    @Id
    val id: String,
    val readerId: String,
    @Enumerated(EnumType.STRING)
    val status: JpaPaymentStatus,
    @Enumerated(EnumType.STRING)
    val type: JpaPaymentType,
    val amount: Float,
    val currency: String,
    val bookId: String,
    val chapterId: String?,
    val startedAt: Instant,
    val completedAt: Instant?
)

enum class JpaPaymentStatus { PENDING, COMPLETED }
enum class JpaPaymentType { BOOK, CHAPTER }