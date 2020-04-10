package com.readify.readerlibrary.infrastructure.domain.payment

import com.readify.readerlibrary.domain.book.BookId
import com.readify.readerlibrary.domain.chapter.ChapterId
import com.readify.readerlibrary.domain.payment.BookPayment
import com.readify.readerlibrary.domain.payment.ChapterPayment
import com.readify.readerlibrary.domain.payment.Payment
import com.readify.readerlibrary.domain.payment.PaymentId
import com.readify.readerlibrary.domain.payment.PaymentRepository
import com.readify.readerlibrary.domain.payment.ReaderId
import com.readify.readerlibrary.domain.payment.Status
import com.readify.readerlibrary.infrastructure.jpa.readerlibrary.JpaPayment
import com.readify.readerlibrary.infrastructure.jpa.readerlibrary.JpaPaymentStatus
import com.readify.readerlibrary.infrastructure.jpa.readerlibrary.JpaPaymentType
import com.readify.readerlibrary.infrastructure.jpa.readerlibrary.ReaderLibraryJpaPaymentDataSource
import com.readify.shared.domain.clock.Clock
import com.readify.shared.domain.money.Currency
import com.readify.shared.domain.money.Money

class JpaPaymentRepository(private val jpaPaymentDataSource: ReaderLibraryJpaPaymentDataSource) : PaymentRepository {
    override fun save(payment: Payment) {
        jpaPaymentDataSource.save(payment.toJpa())
    }

    override fun findById(paymentId: PaymentId) =
        jpaPaymentDataSource.findById(paymentId.value)
            .map { it.toDomain() }
            .orElse(null)
}

private fun JpaPayment.toDomain() =
    when (type) {
        JpaPaymentType.BOOK -> BookPayment(
            PaymentId(id), ReaderId(readerId), status.toDomain(), Money(amount, Currency.valueOf(currency)),
            BookId(bookId), Clock().from(startedAt), completedAt?.let { Clock().from(it) }
        )
        JpaPaymentType.CHAPTER -> ChapterPayment(
            PaymentId(id), ReaderId(readerId), status.toDomain(), Money(amount, Currency.valueOf(currency)),
            BookId(bookId), ChapterId(chapterId!!), Clock().from(startedAt), completedAt?.let { Clock().from(it) }
        )
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


private fun JpaPaymentStatus.toDomain() =
    when (this) {
        JpaPaymentStatus.PENDING -> Status.PENDING
        JpaPaymentStatus.COMPLETED -> Status.COMPLETED
    }
