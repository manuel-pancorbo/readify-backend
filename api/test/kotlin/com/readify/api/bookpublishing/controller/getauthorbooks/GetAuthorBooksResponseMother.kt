package com.readify.api.bookpublishing.controller.getauthorbooks

import com.readify.bookpublishing.application.service.common.BookResponse
import com.readify.bookpublishing.application.service.common.BookStatus
import com.readify.bookpublishing.application.service.common.BookVisibility
import com.readify.bookpublishing.application.service.getauthorbooks.GetAuthorBooksResponse
import java.time.ZonedDateTime
import java.util.UUID

class GetAuthorBooksResponseMother {
    fun singleBookResponse(authorId: String) =
        GetAuthorBooksResponse(
            listOf(
                BookResponse(
                    authorId,
                    UUID.randomUUID().toString(),
                    TITLE,
                    SUMMARY,
                    COVER,
                    tags,
                    PRICE,
                    CURRENCY,
                    BookStatus.IN_PROGRESS,
                    BookVisibility.RESTRICTED,
                    ZonedDateTime.now(),
                    COMPLETION_PERCENTAGE
                )
            )
        )

    companion object {
        private const val TITLE = "Harry Potter and the philosopher's stone"
        private const val SUMMARY =
            "Harry hasn't had a birthday party in eleven years - but all that is about to change when a mysterious letter arrives with an invitation to an incredible place."
        private const val COVER = "https://images-na.ssl-images-amazon.com/images/I/51HSkTKlauL._SX346_BO1,204,203,200_.jpg"
        private const val PRICE = 15f
        private const val CURRENCY = "EUR"
        private const val COMPLETION_PERCENTAGE = 85
        private val tags = listOf("fantasy", "magic")
    }
}
