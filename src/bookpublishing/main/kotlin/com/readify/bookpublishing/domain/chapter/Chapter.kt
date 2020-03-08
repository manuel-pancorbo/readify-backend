package com.readify.bookpublishing.domain.chapter

import com.readify.bookpublishing.domain.book.AuthorId
import com.readify.bookpublishing.domain.book.BookId
import com.readify.shared.domain.clock.Clock
import com.readify.shared.domain.event.RootAggregate
import com.readify.shared.domain.event.book.ChapterCreated
import java.time.ZonedDateTime
import java.util.UUID

data class Chapter(
    val id: ChapterId,
    val title: Title,
    val content: Content,
    val authorId: AuthorId,
    val bookId: BookId,
    val modifiedAt: ZonedDateTime = Clock().now()
) : RootAggregate() {
    companion object {
        fun create(title: Title, content: Content, authorId: AuthorId, bookId: BookId) =
            Chapter(ChapterId(UUID.randomUUID().toString()), title, content, authorId, bookId, Clock().now())
                .also { it.record(ChapterCreated(it.id.value, it.title.value, it.authorId.value, it.bookId.value)) }
    }
}

data class Title(val value: String)
data class Content(val value: String)
data class ChapterId(val value: String) {
    init {
        UUID.fromString(value)
    }
}