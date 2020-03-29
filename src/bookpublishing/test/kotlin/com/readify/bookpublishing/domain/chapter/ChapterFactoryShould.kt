package com.readify.bookpublishing.domain.chapter

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotNull
import com.readify.bookpublishing.domain.book.AuthorId
import com.readify.bookpublishing.domain.book.BookId
import com.readify.shared.domain.event.book.ChapterCreated
import com.readify.shared.domain.event.bus.EventBus
import com.readify.shared.domain.money.Currency.EUR
import com.readify.shared.domain.money.Money
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import java.util.UUID

class ChapterFactoryShould {
    private val eventBus: EventBus = mockk(relaxed = true)
    private val chapterFactory = ChapterFactory(eventBus)

    @Test
    fun `create chapter and publish domain events`() {
        val authorId = AuthorId(UUID.randomUUID().toString())
        val bookId = BookId(UUID.randomUUID().toString())

        val actual = chapterFactory.create(authorId, bookId, "any title", "any chapter content", Money(1.3f, EUR),
            Order(1), Excerpt("any excerpt"))

        assertThat(actual.authorId).isEqualTo(authorId)
        assertThat(actual.bookId).isEqualTo(bookId)
        assertThat(actual.id).isNotNull()
        assertThat(actual.title).isEqualTo(Title("any title"))
        assertThat(actual.content).isEqualTo(Content("any chapter content"))
        assertThat(actual.price).isEqualTo(Money(1.3f, EUR))
        assertThat(actual.modifiedAt).isNotNull()
        assertThat(actual.order).isEqualTo(Order(1))
        assertThat(actual.excerpt).isEqualTo(Excerpt("any excerpt"))
        verify { eventBus.publish(actual.pullDomainEvents()) }
        assertThat(actual.pullDomainEvents()).hasSize(1)
        assertThat(actual.pullDomainEvents()[0]).isInstanceOf(ChapterCreated::class)
    }
}