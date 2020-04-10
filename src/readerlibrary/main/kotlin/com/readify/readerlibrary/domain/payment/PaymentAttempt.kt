package com.readify.readerlibrary.domain.payment

import com.readify.readerlibrary.domain.book.Book
import com.readify.readerlibrary.domain.chapter.Chapter
import com.readify.shared.domain.money.Money

sealed class PaymentAttempt {
    companion object {
        fun of(readerId: ReaderId, book: Book, chapter: Chapter?): PaymentAttempt {
            return chapter?.let { ChapterPaymentAttempt(readerId, book, chapter) }
                ?: BookPaymentAttempt(readerId, book)
        }
    }

    abstract fun getPrice(): Money
    abstract fun getPaymentDescription(): String
    abstract fun getPaymentImage(): String
}

data class BookPaymentAttempt(val readerId: ReaderId, val book: Book) : PaymentAttempt() {
    override fun getPrice() = book.price
    override fun getPaymentDescription() = book.title.value
    override fun getPaymentImage() = book.cover.value
}

data class ChapterPaymentAttempt(val readerId: ReaderId, val book: Book, val chapter: Chapter) : PaymentAttempt() {
    override fun getPrice() = chapter.price
    override fun getPaymentDescription() = "${book.title.value}: Chapter ${chapter.order.value}. ${chapter.title.value}"
    override fun getPaymentImage() = book.cover.value
}