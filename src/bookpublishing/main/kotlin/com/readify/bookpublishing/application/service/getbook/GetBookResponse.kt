package com.readify.bookpublishing.application.service.getbook

import com.readify.bookpublishing.application.service.common.BookStatus
import com.readify.bookpublishing.application.service.common.BookVisibility
import java.time.ZonedDateTime

sealed class GetBookResponse
object BookNotFoundResponse : GetBookResponse()
data class BookFoundResponse(
    val authorId: String,
    val bookId: String,
    val title: String,
    val summary: String,
    val cover: String,
    val tags: List<String>,
    val priceAmount: Float,
    val priceCurrency: String,
    val status: BookStatus,
    val visibility: BookVisibility,
    val finishedAt: ZonedDateTime?,
    val completionPercentage: Int
) : GetBookResponse()