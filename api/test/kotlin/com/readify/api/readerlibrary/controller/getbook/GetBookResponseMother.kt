package com.readify.api.readerlibrary.controller.getbook

import com.readify.readerlibrary.application.service.getbook.BookResponse
import com.readify.readerlibrary.application.service.getbook.BookStatusResponse
import com.readify.readerlibrary.application.service.getbook.ChapterResponse
import com.readify.readerlibrary.domain.book.CompletionPercentage
import com.readify.shared.domain.book.Status
import com.readify.shared.domain.clock.Clock
import com.readify.shared.domain.money.Currency
import java.util.UUID

class GetBookResponseMother {
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
            listOf(anyChapter()),
            Clock().now()
        )

    private fun anyChapter() =
        ChapterResponse(
            chapterId,
            chapterTitle,
            1.43f,
            Currency.EUR.toString(),
            1,
            chapterExcerpt
        )

    companion object {
        private val authorId = UUID.randomUUID().toString()
        private val title = "Harry Potter and the philosopher's stone"
        private val tags = listOf("fantasy", "magic")
        private val completionPercentage = 100
        private val chapterId = UUID.randomUUID().toString()
        private val chapterTitle = "The boy who lived"
        private val chapterExcerpt = "Some excerpt"
    }
}