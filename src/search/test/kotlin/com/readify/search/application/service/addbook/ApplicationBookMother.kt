package com.readify.search.application.service.addbook

import com.readify.search.application.service.common.ApplicationBook
import com.readify.search.application.service.common.ApplicationBookStatus
import java.util.UUID

class ApplicationBookMother {
    fun inProgressOne() =
        ApplicationBook(
            ID,
            AUTHOR_ID,
            TITLE,
            COVER,
            SUMMARY,
            TAGS,
            PRICE,
            CURRENCY,
            COMPLETION_PERCENTAGE,
            ApplicationBookStatus.IN_PROGRESS,
            null
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