package com.readify.bookpublishing.infrastructure.jpa.chapter

import com.readify.readerlibrary.domain.book.BookId
import com.readify.readerlibrary.domain.chapter.ChapterId
import com.readify.readerlibrary.domain.payment.BookPayment
import com.readify.readerlibrary.domain.payment.ChapterPayment
import com.readify.readerlibrary.domain.payment.PaymentId
import com.readify.readerlibrary.domain.payment.ReaderId
import com.readify.readerlibrary.domain.payment.Status
import com.readify.shared.domain.money.Currency
import com.readify.shared.domain.money.Money
import java.util.UUID

class PaymentMother {
    companion object {
        fun pendingBookPayment(id: String) = BookPayment(
            PaymentId(id), ReaderId(readerId), Status.PENDING, amount, BookId(bookId)
        )

        fun completedBookPayment(id: String) = BookPayment(
            PaymentId(id), ReaderId(readerId), Status.PENDING, amount, BookId(bookId)
        )

        fun pendingChapterPayment(id: String) = ChapterPayment(
            PaymentId(id), ReaderId(readerId), Status.PENDING, amount, BookId(bookId), ChapterId(chapterId)
        )

        fun completedChapterPayment(id: String) = ChapterPayment(
            PaymentId(id), ReaderId(readerId), Status.PENDING, amount, BookId(bookId), ChapterId(chapterId)
        )

        private val readerId = UUID.randomUUID().toString()
        private val bookId = UUID.randomUUID().toString()
        private val chapterId = UUID.randomUUID().toString()
        private val amount = Money(14.2f, Currency.EUR)
    }
}