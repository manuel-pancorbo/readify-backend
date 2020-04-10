package com.readify.readerlibrary.domain.payment

import com.readify.readerlibrary.domain.book.BookId
import com.readify.readerlibrary.domain.chapter.ChapterId
import com.readify.shared.domain.clock.Clock
import com.readify.shared.domain.money.Currency
import com.readify.shared.domain.money.Money
import java.util.UUID

class PaymentMother {
    companion object {
        fun pendingBookPayment(id: String) = BookPayment(
            PaymentId(id), ReaderId(readerId), Status.PENDING, amount, BookId(bookId), startedAt, null
        )

        fun completedBookPayment(id: String) = BookPayment(
            PaymentId(id), ReaderId(readerId), Status.COMPLETED, amount, BookId(bookId), startedAt, completedAt
        )

        fun pendingChapterPayment(id: String) = ChapterPayment(
            PaymentId(id), ReaderId(readerId), Status.PENDING, amount, BookId(bookId), ChapterId(chapterId), startedAt,
            null
        )

        fun completedChapterPayment(id: String) = ChapterPayment(
            PaymentId(id), ReaderId(readerId), Status.COMPLETED, amount, BookId(bookId), ChapterId(chapterId),
            startedAt, completedAt
        )

        private val readerId = UUID.randomUUID().toString()
        private val bookId = UUID.randomUUID().toString()
        private val chapterId = UUID.randomUUID().toString()
        private val amount = Money(14.2f, Currency.EUR)
        private val startedAt = Clock().now()
        private val completedAt = Clock().now()
    }
}