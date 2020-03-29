package com.readify.bookpublishing.domain.chapter

import com.readify.bookpublishing.domain.book.AuthorId
import com.readify.bookpublishing.domain.book.BookId
import com.readify.shared.domain.clock.Clock
import com.readify.shared.domain.event.RootAggregate
import com.readify.shared.domain.event.book.ChapterCreated
import com.readify.shared.domain.event.book.ChapterPublished
import com.readify.shared.domain.event.book.ChapterUpdated
import com.readify.shared.domain.money.Money
import java.time.ZonedDateTime
import java.util.UUID

sealed class Chapter(
    open val id: ChapterId, open val title: Title, open val content: Content, open val price: Money,
    open val authorId: AuthorId, open val bookId: BookId, open val modifiedAt: ZonedDateTime, open val order: Order,
    open val excerpt: Excerpt?
) : RootAggregate() {
    fun sameAuthor(anotherAuthorId: AuthorId) = authorId == anotherAuthorId
    abstract fun update(title: String?, content: String?, order: Int?, excerpt: String?): Chapter

    companion object {
        fun create(title: Title, content: Content, price: Money, authorId: AuthorId, bookId: BookId, order: Order,
                   excerpt: Excerpt?) =
            DraftChapter(
                ChapterId(UUID.randomUUID().toString()), title, content, price, authorId, bookId, Clock().now(), order,
                excerpt
            )
                .also { it.record(ChapterCreated(it.id.value, it.title.value, it.authorId.value, it.bookId.value,
                    it.order.value, it.excerpt?.value)) }
    }
}

data class DraftChapter(
    override val id: ChapterId, override val title: Title, override val content: Content, override val price: Money,
    override val authorId: AuthorId, override val bookId: BookId, override val modifiedAt: ZonedDateTime,
    override val order: Order, override val excerpt: Excerpt?
) : Chapter(id, title, content, price, authorId, bookId, modifiedAt, order, excerpt) {
    override fun update(title: String?, content: String?, order: Int?, excerpt: String?) =
        copy(
            title = title?.let { Title(it) } ?: this.title,
            content = content?.let { Content(it) } ?: this.content,
            order = order?.let { Order(order) } ?: this.order,
            excerpt = excerpt?.let { Excerpt(excerpt) } ?: this.excerpt,
            modifiedAt = Clock().now()
        )
            .also {
                it.record(
                    ChapterUpdated(
                        it.bookId.value, it.id.value, it.authorId.value, it.title.value,
                        it.modifiedAt, it.order.value, it.excerpt?.value
                    )
                )
            }

    fun publish() = PublishedChapter(
        id, title, content, price, authorId, bookId, modifiedAt, order, excerpt, Clock().now()
    )
        .also { it.record(ChapterPublished(it.id.value, it.publishedAt)) }
}

data class PublishedChapter(
    override val id: ChapterId, override val title: Title, override val content: Content, override val price: Money,
    override val authorId: AuthorId, override val bookId: BookId, override val modifiedAt: ZonedDateTime,
    override val order: Order, override val excerpt: Excerpt?, val publishedAt: ZonedDateTime = Clock().now()
) : Chapter(id, title, content, price, authorId, bookId, modifiedAt, order, excerpt) {
    override fun update(title: String?, content: String?, order: Int?, excerpt: String?) =
        copy(
            title = title?.let { Title(it) } ?: this.title,
            content = content?.let { Content(it) } ?: this.content,
            order = order?.let { Order(order) } ?: this.order,
            excerpt = excerpt?.let { Excerpt(excerpt) } ?: this.excerpt,
            modifiedAt = Clock().now()
        )
            .also {
                it.record(
                    ChapterUpdated(
                        it.bookId.value, it.id.value, it.authorId.value, it.title.value,
                        it.modifiedAt, it.order.value, it.excerpt?.value
                    )
                )
            }

}
