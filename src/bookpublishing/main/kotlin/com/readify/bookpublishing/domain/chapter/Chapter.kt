package com.readify.bookpublishing.domain.chapter

import com.readify.bookpublishing.domain.book.AuthorId
import com.readify.bookpublishing.domain.book.BookId
import com.readify.shared.domain.clock.Clock
import com.readify.shared.domain.event.RootAggregate
import com.readify.shared.domain.event.book.ChapterCreated
import com.readify.shared.domain.money.Money
import java.time.ZonedDateTime
import java.util.StringTokenizer
import java.util.UUID

sealed class Chapter : RootAggregate()

class DraftChapter(
    val id: ChapterId,
    val title: Title,
    val content: Content,
    val price: Money,
    val authorId: AuthorId,
    val bookId: BookId,
    val modifiedAt: ZonedDateTime
) : Chapter() {
    companion object {
        fun create(title: Title, content: Content, price: Money, authorId: AuthorId, bookId: BookId) =
            DraftChapter(ChapterId(UUID.randomUUID().toString()), title, content, price, authorId, bookId, Clock().now())
                .also { it.record(ChapterCreated(it.id.value, it.title.value, it.authorId.value, it.bookId.value)) }
    }

    fun publish() =
        PublishedChapter(id, title, content, price, authorId, bookId, modifiedAt, Clock().now())
}

class PublishedChapter(
    val id: ChapterId, val title: Title, val content: Content, val price: Money, val authorId: AuthorId,
    val bookId: BookId, val modifiedAt: ZonedDateTime, val publishedAt: ZonedDateTime
) : Chapter()

data class Title(val value: String)
data class Content(val value: String) {
    val wordCount: Int = StringTokenizer(value).countTokens()

    init {
        if (wordCount > 10000) throw IllegalArgumentException()
    }
}

data class ChapterId(val value: String) {
    init {
        UUID.fromString(value)
    }
}