package com.readify.bookpublishing.domain.book

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import com.readify.shared.domain.event.bus.EventBus
import com.readify.shared.domain.money.Currency
import com.readify.shared.domain.money.Money
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

class BookFactoryShould {

    private val eventBus: EventBus = mockk(relaxed = true)
    private val bookFactory = BookFactory(eventBus)

    @Test
    fun `create valid user with random uuid and publish domain event`() {

        val book = bookFactory.create(AuthorId(AUTHOR_ID), Title(TITLE), Cover(COVER), Summary(SUMMARY), Tags(tags),
            Money(PRICE_AMOUNT, priceCurrency))

        verify { eventBus.publish(book.pullDomainEvents()) }
        assertThat(book.id.value).isNotNull()
        assertThat(book.authorId.value).isEqualTo(AUTHOR_ID)
        assertThat(book.title.value).isEqualTo(TITLE)
        assertThat(book.summary.value).isEqualTo(SUMMARY)
        assertThat(book.cover.value).isEqualTo(COVER)
        assertThat(book.tags.value).isEqualTo(tags)
        assertThat(book.price.amount).isEqualTo(PRICE_AMOUNT)
        assertThat(book.price.currency).isEqualTo(priceCurrency)
    }

    companion object {
        const val AUTHOR_ID = "0b35b63a-c7b6-4faf-bda1-c95868def3c7"
        const val TITLE = "Harry Potter and the philosopher's stone"
        const val SUMMARY =
            "Harry hasn't had a birthday party in eleven years - but all that is about to change when a mysterious letter arrives with an invitation to an incredible place."
        const val COVER = "https://images-na.ssl-images-amazon.com/images/I/51HSkTKlauL._SX346_BO1,204,203,200_.jpg"
        const val PRICE_AMOUNT = 15f
        val priceCurrency = Currency.EUR
        val tags = listOf("fantasy", "magic")
    }
}