package com.readify.api.readerlibrary.controller.getbooksbyids

import com.readify.readerlibrary.application.service.common.BookResponse
import com.readify.readerlibrary.application.service.common.StatusResponse
import com.readify.readerlibrary.domain.book.BookId
import com.readify.shared.domain.clock.Clock
import java.util.UUID

class BookResponseMother {
    fun inProgressOne(id: String = ID) =
        BookResponse(
            id,
            AUTHOR_ID,
            TITLE,
            COVER,
            SUMMARY,
            TAGS,
            PRICE,
            CURRENCY,
            COMPLETION_PERCENTAGE,
            StatusResponse.IN_PROGRESS,
            null
        )

    fun finishedOne(id: String = ID) =
        BookResponse(
            id,
            AUTHOR_ID,
            TITLE,
            COVER,
            SUMMARY,
            TAGS,
            PRICE,
            CURRENCY,
            100,
            StatusResponse.FINISHED,
            Clock().now()
        )

    companion object {
        private val ID = UUID.randomUUID().toString()
        private val AUTHOR_ID = UUID.randomUUID().toString()
        private const val COMPLETION_PERCENTAGE = 75
        private const val TITLE = "Harry Potter and the philosopher's stone"
        private const val SUMMARY =
            "Harry hasn't had a birthday party in eleven years - but all that is about to change when a mysterious letter arrives with an invitation to an incredible place."
        private const val COVER =
            "https://images-na.ssl-images-amazon.com/images/I/51HSkTKlauL._SX346_BO1,204,203,200_.jpg"
        private const val PRICE = 15f
        private const val CURRENCY = "EUR"
        private val TAGS = listOf("fantasy", "magic")
    }
}