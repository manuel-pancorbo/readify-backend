package com.readify.bookpublishing.domain.book

import com.readify.shared.domain.event.RootAggregate
import com.readify.shared.domain.event.book.BookPublished
import java.util.UUID

data class Book(
    val id: BookId,
    val authorId: AuthorId,
    val title: Title,
    val cover: Cover,
    val summary: Summary,
    val tags: Tags
) :
    RootAggregate() {
    companion object {
        fun create(id: BookId, authorId: AuthorId, title: Title, cover: Cover, summary: Summary, tags: Tags) =
            Book(id, authorId, title, cover, summary, tags)
                .also { it.record(BookPublished(id.value, authorId.value, title.value, cover.value, summary.value, tags.value)) }
    }
}

data class BookId(val value: String) {
    init {
        UUID.fromString(value)
    }
}

data class AuthorId(val value: String)
data class Title(val value: String)
data class Summary(val value: String)
data class Cover(val value: String)
data class Tags(val value: List<String>)