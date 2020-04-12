package com.readify.search.application.service.searchbooks

import com.readify.search.application.service.common.ApplicationBook
import com.readify.search.application.service.common.ApplicationBookStatus
import com.readify.search.domain.book.AuthorId
import com.readify.search.domain.book.Book
import com.readify.search.domain.book.BookId
import com.readify.search.domain.book.BookStatus
import com.readify.search.domain.book.CompletionPercentage
import com.readify.search.domain.book.Cover
import com.readify.search.domain.book.Summary
import com.readify.search.domain.book.Tags
import com.readify.search.domain.book.Title
import com.readify.shared.domain.clock.Clock
import com.readify.shared.domain.money.Currency
import com.readify.shared.domain.money.Money
import java.util.UUID

class BookMother {
    fun finishedOne() =
        Book(
            BookId(ID),
            AuthorId(AUTHOR_ID),
            Title(TITLE),
            Cover(COVER),
            Summary(SUMMARY),
            Tags(TAGS),
            Money(PRICE, Currency.valueOf(CURRENCY)),
            CompletionPercentage(COMPLETION_PERCENTAGE),
            BookStatus.FINISHED,
            Clock().now()
        )

    companion object {
        private val ID = UUID.randomUUID().toString()
        private val AUTHOR_ID = UUID.randomUUID().toString()
        private const val COMPLETION_PERCENTAGE = 75
        private const val TITLE = "Harry Potter and the philosopher's stone"
        private const val SUMMARY =
            "Harry hasn't had a birthday party in eleven years - but all that is about to change when a mysterious letter arrives with an invitation to an incredible place."
        private const val COVER = "https://images-na.ssl-images-amazon.com/images/I/51HSkTKlauL._SX346_BO1,204,203,200_.jpg"
        private const val PRICE = 15f
        private const val CURRENCY = "EUR"
        private val TAGS = listOf("fantasy", "magic")
    }
}