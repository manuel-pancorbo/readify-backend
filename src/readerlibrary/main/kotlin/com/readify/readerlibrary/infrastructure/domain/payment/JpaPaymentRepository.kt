package com.readify.readerlibrary.infrastructure.domain.payment

import com.readify.readerlibrary.domain.payment.BookPayment
import com.readify.readerlibrary.domain.payment.ChapterPayment
import com.readify.readerlibrary.domain.payment.Payment
import com.readify.readerlibrary.domain.payment.PaymentRepository
import com.readify.readerlibrary.domain.payment.Status
import com.readify.readerlibrary.infrastructure.jpa.readerlibrary.JpaPayment
import com.readify.readerlibrary.infrastructure.jpa.readerlibrary.JpaPaymentStatus
import com.readify.readerlibrary.infrastructure.jpa.readerlibrary.JpaPaymentType
import com.readify.readerlibrary.infrastructure.jpa.readerlibrary.ReaderLibraryJpaPaymentDataSource

class JpaPaymentRepository(private val jpaPaymentDataSource: ReaderLibraryJpaPaymentDataSource) : PaymentRepository {
    override fun save(payment: Payment) {
        jpaPaymentDataSource.save(payment.toJpa())
    }
}

private fun Payment.toJpa() =
    when (this) {
        is BookPayment -> JpaPayment(
            id.value, readerId.value, status.toJpa(), JpaPaymentType.BOOK, amount.amount,
            amount.currency.toString(), bookId.value, null, startedAt.toInstant(), completedAt?.toInstant()
        )
        is ChapterPayment -> JpaPayment(
            id.value, readerId.value, status.toJpa(), JpaPaymentType.CHAPTER, amount.amount,
            amount.currency.toString(), bookId.value, chapterId.value, startedAt.toInstant(), completedAt?.toInstant()
        )
    }

private fun Status.toJpa() =
    when (this) {
        Status.PENDING -> JpaPaymentStatus.PENDING
        Status.COMPLETED -> JpaPaymentStatus.COMPLETED
    }
