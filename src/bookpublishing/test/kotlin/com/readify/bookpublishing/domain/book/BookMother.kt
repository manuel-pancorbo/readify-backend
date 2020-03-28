package com.readify.bookpublishing.domain.book

import com.readify.shared.domain.money.Currency
import com.readify.shared.domain.money.Money

class BookMother {
    fun inProgressBook(bookId: String, authorId: String, completionPercentage: Int = 0) =
        InProgressBook(
            BookId(bookId),
            AuthorId(authorId),
            Title("Harry Potter and the philosopher's stone"),
            Cover("https://images-na.ssl-images-amazon.com/images/I/51HSkTKlauL._SX346_BO1,204,203,200_.jpg"),
            Summary("Harry hasn't had a birthday party in eleven years - but all that is about to change when a mysterious letter arrives with an invitation to an incredible place."),
            Tags(listOf("fantasy", "magic")),
            Money(1f, Currency.EUR),
            CompletionPercentage(completionPercentage)
        )


    fun finishedBook(bookId: String, authorId: String) =
        FinishedBook(
            BookId(bookId),
            AuthorId(authorId),
            Title("Harry Potter and the philosopher's stone"),
            Cover("https://images-na.ssl-images-amazon.com/images/I/51HSkTKlauL._SX346_BO1,204,203,200_.jpg"),
            Summary("Harry hasn't had a birthday party in eleven years - but all that is about to change when a mysterious letter arrives with an invitation to an incredible place."),
            Tags(listOf("fantasy", "magic")),
            Money(1f, Currency.EUR),
            Visibility.NULL
        )
}