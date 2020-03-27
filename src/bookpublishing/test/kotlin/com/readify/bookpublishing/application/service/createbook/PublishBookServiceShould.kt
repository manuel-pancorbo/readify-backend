package com.readify.bookpublishing.application.service.createbook

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.readify.bookpublishing.domain.book.AuthorId
import com.readify.bookpublishing.domain.book.DraftBook
import com.readify.bookpublishing.domain.book.BookFactory
import com.readify.bookpublishing.domain.book.BookId
import com.readify.bookpublishing.domain.book.BookRepository
import com.readify.bookpublishing.domain.book.Cover
import com.readify.bookpublishing.domain.book.Summary
import com.readify.bookpublishing.domain.book.Tags
import com.readify.bookpublishing.domain.book.Title
import com.readify.shared.domain.money.Currency
import com.readify.shared.domain.money.Money
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

class PublishBookServiceShould {

    private val bookRepository: BookRepository = mockk(relaxed = true)
    private val bookFactory: BookFactory = mockk(relaxed = true)
    private val service = PublishBookService(bookFactory, bookRepository)

    @Test
    fun `save a new book and publish a book created domain event`() {
        every {
            bookFactory.create(
                AuthorId(AUTHOR_ID), Title(TITLE), Cover(COVER), Summary(SUMMARY), Tags(tags), Money(
                    PRICE_AMOUNT, Currency.EUR
                )
            )
        }.returns(book())

        val response = service.execute(request())

        verify { bookRepository.save(book()) }
        assertThat(response).isEqualTo(expectedResponse())
    }

    @Test
    fun `return invalid currency response when given price currency is not supported`() {
        val response = service.execute(request(currency = "non-supported-currency"))

        assertThat(response).isEqualTo(InvalidCurrencyResponse)
    }

    private fun request(currency: String = PRICE_CURRENCY) =
        PublishBookRequest(AUTHOR_ID, TITLE, SUMMARY, COVER, tags, PRICE_AMOUNT, currency)
    private fun expectedResponse() =
        BookPublishedSuccessfullyResponse(AUTHOR_ID, BOOK_ID, TITLE, SUMMARY, COVER, tags, PRICE_AMOUNT,
            PRICE_CURRENCY, BookStatus.DRAFT, null, 0)
    private fun book() = DraftBook(BookId(BOOK_ID), AuthorId(AUTHOR_ID), Title(TITLE), Cover(COVER), Summary(SUMMARY),
        Tags(tags), Money(PRICE_AMOUNT, Currency.EUR))

    companion object {
        const val BOOK_ID = "71ede130-a7d2-4726-8702-90383dc5cd7d"
        const val AUTHOR_ID = "0b35b63a-c7b6-4faf-bda1-c95868def3c7"
        const val TITLE = "Harry Potter and the philosopher's stone"
        const val SUMMARY =
            "Harry hasn't had a birthday party in eleven years - but all that is about to change when a mysterious letter arrives with an invitation to an incredible place."
        const val COVER = "https://images-na.ssl-images-amazon.com/images/I/51HSkTKlauL._SX346_BO1,204,203,200_.jpg"
        const val PRICE_AMOUNT = 15f
        const val PRICE_CURRENCY = "EUR"
        val tags = listOf("fantasy", "magic")
    }
}