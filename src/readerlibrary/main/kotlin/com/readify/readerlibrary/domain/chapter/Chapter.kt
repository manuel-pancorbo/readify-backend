package com.readify.readerlibrary.domain.chapter

import com.readify.readerlibrary.domain.book.AuthorId
import com.readify.readerlibrary.domain.book.Book
import com.readify.readerlibrary.domain.book.BookId
import com.readify.shared.domain.money.Money
import java.time.ZonedDateTime

class Chapter(
    val id: ChapterId, val title: Title, val content: Content, val price: Money, val authorId: AuthorId,
    val bookId: BookId, val modifiedAt: ZonedDateTime, val order: Order, val excerpt: Excerpt?,
    val publishedAt: ZonedDateTime
) {
    fun belongsTo(book: Book) = bookId == book.id
}

data class Title(val value: String)
data class Content(val value: String)
data class ChapterId(val value: String)
data class Order(val value: Int)
data class Excerpt(val value: String)