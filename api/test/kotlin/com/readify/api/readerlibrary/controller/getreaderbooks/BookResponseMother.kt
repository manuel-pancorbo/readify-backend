package com.readify.api.readerlibrary.controller.getreaderbooks

import com.readify.readerlibrary.application.service.getreaderbooks.BookResponse
import com.readify.readerlibrary.application.service.getreaderbooks.BookStatusResponse
import com.readify.shared.domain.clock.Clock
import com.readify.shared.domain.money.Currency
import java.util.UUID

class BookResponseMother {
    fun createFinishedBook(id: String) =
        BookResponse(
            id,
            authorId,
            title,
            "https://images-na.ssl-images-amazon.com/images/I/51HSkTKlauL._SX346_BO1,204,203,200_.jpg",
            "Harry hasn't had a birthday party in eleven years - but all that is about to change when a mysterious letter arrives with an invitation to an incredible place.",
            tags,
            12f,
            Currency.EUR.toString(),
            completionPercentage,
            BookStatusResponse.FINISHED,
            Clock().now()
        )

    companion object {
        private val authorId = UUID.randomUUID().toString()
        private val title = "Harry Potter and the philosopher's stone"
        private val tags = listOf("fantasy", "magic")
        private val completionPercentage = 100
    }
}