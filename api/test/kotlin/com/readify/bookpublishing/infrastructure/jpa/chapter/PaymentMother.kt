package com.readify.bookpublishing.infrastructure.jpa.chapter

import com.readify.readerlibrary.domain.book.BookId
import com.readify.readerlibrary.domain.chapter.ChapterId
import com.readify.readerlibrary.domain.payment.BookPayment
import com.readify.readerlibrary.domain.payment.ChapterPayment
import com.readify.readerlibrary.domain.payment.PaymentId
import com.readify.readerlibrary.domain.payment.ReaderId
import com.readify.readerlibrary.domain.payment.Status
import com.readify.shared.domain.clock.Clock
import com.readify.shared.domain.money.Currency
import com.readify.shared.domain.money.Money
import java.time.ZonedDateTime
import java.util.UUID

class PaymentMother {
    companion object {
        fun pendingBookPayment(
            id: String,
            readerId: String = this.readerId
        ) = BookPayment(
            PaymentId(id), ReaderId(readerId), Status.PENDING, amount, BookId(bookId), startedAt, null
        )

        fun completedBookPayment(
            id: String,
            completedAt: ZonedDateTime = Clock().now(),
            readerId: String = this.readerId
        ) = BookPayment(
            PaymentId(id), ReaderId(readerId), Status.COMPLETED, amount, BookId(bookId), startedAt, completedAt
        )

        fun pendingChapterPayment(
            id: String,
            readerId: String = this.readerId
        ) = ChapterPayment(
            PaymentId(id), ReaderId(readerId), Status.PENDING, amount, BookId(bookId), ChapterId(chapterId),
            startedAt, null
        )

        fun completedChapterPayment(
            id: String,
            completedAt: ZonedDateTime = Clock().now(),
            readerId: String = this.readerId
        ) = ChapterPayment(
            PaymentId(id), ReaderId(readerId), Status.COMPLETED, amount, BookId(bookId), ChapterId(chapterId),
            startedAt, completedAt
        )

        private val readerId = UUID.randomUUID().toString()
        private val bookId = UUID.randomUUID().toString()
        private val chapterId = UUID.randomUUID().toString()
        private val amount = Money(14.2f, Currency.EUR)
        private val startedAt = Clock().now()
    }
}