package com.readify.bookpublishing.domain.book

import com.readify.shared.domain.clock.Clock
import com.readify.shared.domain.event.RootAggregate
import com.readify.shared.domain.event.book.BookPublished
import com.readify.shared.domain.money.Money
import java.time.ZonedDateTime
import java.util.UUID

sealed class Book(
    open val id: BookId, open val authorId: AuthorId, open val title: Title, open val cover: Cover,
    open val summary: Summary, open val tags: Tags, open val price: Money, open val completionPercentage: Int
) : RootAggregate() {

    fun sameAuthor(anotherAuthorId: AuthorId) = authorId == anotherAuthorId

    companion object {
        fun create(id: BookId, authorId: AuthorId, title: Title, cover: Cover, summary: Summary, tags: Tags,
                   price: Money) =
            InProgressBook(id, authorId, title, cover, summary, tags, price)
                .also { it.record(BookPublished(id.value, authorId.value, title.value, cover.value, summary.value,
                    tags.value, price)) }
    }
}

data class InProgressBook(
    override val id: BookId, override val authorId: AuthorId, override val title: Title, override val cover: Cover,
    override val summary: Summary, override val tags: Tags, override val price: Money,
    override val completionPercentage: Int = 0
) : Book(id, authorId, title, cover, summary, tags, price, completionPercentage)

data class FinishedBook(
    override val id: BookId, override val authorId: AuthorId, override val title: Title, override val cover: Cover,
    override val summary: Summary, override val tags: Tags, override val price: Money,
    override val completionPercentage: Int, val finishedAt: ZonedDateTime = Clock().now()
) : Book(id, authorId, title, cover, summary, tags, price, completionPercentage)

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