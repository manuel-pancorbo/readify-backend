package com.readify.api.bookpublishing.controller.getbookdetails

import com.readify.bookpublishing.application.service.common.BookStatus
import com.readify.bookpublishing.application.service.common.BookVisibility
import com.readify.bookpublishing.application.service.getbook.BookFoundResponse
import java.time.ZonedDateTime

class GetBookResponseMother {
    fun anyBook(authorId: String, bookId: String) =
        BookFoundResponse(
            authorId,
            bookId,
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

    companion object {
        const val TITLE = "Harry Potter and the philosopher's stone"
        const val SUMMARY =
            "Harry hasn't had a birthday party in eleven years - but all that is about to change when a mysterious letter arrives with an invitation to an incredible place."
        const val COVER = "https://images-na.ssl-images-amazon.com/images/I/51HSkTKlauL._SX346_BO1,204,203,200_.jpg"
        const val PRICE = 15f
        const val CURRENCY = "EUR"
        const val COMPLETION_PERCENTAGE = 85
        val tags = listOf("fantasy", "magic")
    }
}
