package com.readify.bookpublishing.domain.book

import com.readify.bookpublishing.domain.book.CompletionPercentage.Companion.empty
import com.readify.bookpublishing.domain.book.CompletionPercentage.Companion.finished
import com.readify.bookpublishing.domain.book.Visibility.NULL
import com.readify.shared.domain.clock.Clock
import com.readify.shared.domain.event.RootAggregate
import com.readify.shared.domain.money.Money
import java.time.ZonedDateTime

sealed class Book(
    open val id: BookId, open val authorId: AuthorId, open val title: Title, open val cover: Cover,
    open val summary: Summary, open val tags: Tags, open val price: Money,
    open val completionPercentage: CompletionPercentage, open val visibility: Visibility
) : RootAggregate() {

    fun sameAuthor(anotherAuthorId: AuthorId) = authorId == anotherAuthorId
    abstract fun update(newCompletionPercentage: CompletionPercentage): Book

    companion object {
        fun create(
            id: BookId, authorId: AuthorId, title: Title, cover: Cover, summary: Summary, tags: Tags,
            price: Money
        ) =
            InProgressBook(id, authorId, title, cover, summary, tags, price)
                .also { it.record(DomainEventFactory.bookCreated(it)) }
    }
}

data class InProgressBook(
    override val id: BookId, override val authorId: AuthorId, override val title: Title, override val cover: Cover,
    override val summary: Summary, override val tags: Tags, override val price: Money,
    override val completionPercentage: CompletionPercentage = empty(), override val visibility: Visibility = NULL
) : Book(id, authorId, title, cover, summary, tags, price, completionPercentage, visibility) {
    override fun update(newCompletionPercentage: CompletionPercentage) =
        if (newCompletionPercentage.isFinished()) {
            FinishedBook(id, authorId, title, cover, summary, tags, price, visibility)
                .also { it.record(DomainEventFactory.bookFinished(it)) }
        } else {
            copy(completionPercentage = newCompletionPercentage)
                .also { it.record(DomainEventFactory.bookUpdated(it)) }
        }
}

data class FinishedBook(
    override val id: BookId, override val authorId: AuthorId, override val title: Title, override val cover: Cover,
    override val summary: Summary, override val tags: Tags, override val price: Money,
    override val visibility: Visibility, val finishedAt: ZonedDateTime = Clock().now()
) : Book(id, authorId, title, cover, summary, tags, price, finished(), visibility) {
    override fun update(newCompletionPercentage: CompletionPercentage) =
        if (newCompletionPercentage.isFinished()) {
            this
                //update book
                .also { it.record(DomainEventFactory.bookUpdated(it)) }
        } else {
            InProgressBook(id, authorId, title, cover, summary, tags, price, newCompletionPercentage, visibility)
                .also { it.record(DomainEventFactory.bookUpdated(it)) }
        }
}
