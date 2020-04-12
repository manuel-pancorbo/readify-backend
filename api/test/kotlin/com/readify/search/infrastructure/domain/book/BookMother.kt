package com.readify.search.infrastructure.domain.book

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

class BookMother {
    fun finishedBook(
        bookId: String,
        authorId: String,
        completionPercentage: Int = 0,
        title: String = TITLE,
        tags: List<String> = TAGS
    ) =
        Book(
            BookId(bookId),
            AuthorId(authorId),
            Title(title),
            Cover("https://images-na.ssl-images-amazon.com/images/I/51HSkTKlauL._SX346_BO1,204,203,200_.jpg"),
            Summary("Harry hasn't had a birthday party in eleven years - but all that is about to change when a mysterious letter arrives with an invitation to an incredible place."),
            Tags(tags),
            Money(1f, Currency.EUR),
            CompletionPercentage(completionPercentage),
            BookStatus.FINISHED,
            Clock().now()
        )

    companion object {
        private const val TITLE = "Harry Potter and the philosopher's stone"
        private val TAGS = listOf("fantasy", "magic")
    }
}