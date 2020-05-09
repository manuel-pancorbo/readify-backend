package com.readify.api.readerlibrary.controller.common

import com.readify.readerlibrary.application.service.getreaderbookchapter.FullChapterResponse
import com.readify.shared.domain.clock.Clock
import java.util.UUID

class FullChapterResponseMother {
    fun anyChapter(bookId: String, chapterId: String) =
        FullChapterResponse(
            chapterId,
            title,
            content,
            priceAmount,
            priceCurrency,
            authorId,
            bookId,
            Clock().now(),
            order,
            excerpt,
            Clock().now()
        )

    companion object {
        private val title = "any title"
        private val content = """any
            content
        """
        private val priceAmount = 1.25f
        private val priceCurrency = "EUR"
        private val authorId = UUID.randomUUID().toString()
        private val order = 1
        private val excerpt = "any excerpt"
    }
}