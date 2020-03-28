package com.readify.bookpublishing.domain.chapter

import com.readify.bookpublishing.domain.book.AuthorId
import com.readify.bookpublishing.domain.book.BookId
import com.readify.shared.domain.clock.Clock
import com.readify.shared.domain.event.RootAggregate
import com.readify.shared.domain.event.book.ChapterCreated
import com.readify.shared.domain.event.book.ChapterPublished
import com.readify.shared.domain.money.Money
import java.time.ZonedDateTime
import java.util.StringTokenizer
import java.util.UUID

sealed class Chapter(
    open val id: ChapterId, open val title: Title, open val content: Content, open val price: Money,
    open val authorId: AuthorId, open val bookId: BookId, open val modifiedAt: ZonedDateTime
) : RootAggregate() {
    fun sameAuthor(anotherAuthorId: AuthorId) = authorId == anotherAuthorId

    companion object {
        fun create(title: Title, content: Content, price: Money, authorId: AuthorId, bookId: BookId) =
            DraftChapter(
                ChapterId(UUID.randomUUID().toString()), title, content, price, authorId, bookId, Clock().now()
            )
                .also { it.record(ChapterCreated(it.id.value, it.title.value, it.authorId.value, it.bookId.value)) }
    }
}

data class DraftChapter(
    override val id: ChapterId, override val title: Title, override val content: Content, override val price: Money,
    override val authorId: AuthorId, override val bookId: BookId, override val modifiedAt: ZonedDateTime
) : Chapter(id, title, content, price, authorId, bookId, modifiedAt) {
    fun publish() = PublishedChapter(id, title, content, price, authorId, bookId, modifiedAt, Clock().now())
        .also { it.record(ChapterPublished(it.id.value, it.publishedAt)) }
}

data class PublishedChapter(
    override val id: ChapterId, override val title: Title, override val content: Content, override val price: Money,
    override val authorId: AuthorId, override val bookId: BookId, override val modifiedAt: ZonedDateTime,
    val publishedAt: ZonedDateTime = Clock().now()
) : Chapter(id, title, content, price, authorId, bookId, modifiedAt)

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