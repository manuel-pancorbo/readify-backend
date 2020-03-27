package com.readify.bookpublishing.application.service.createbook

import java.time.ZonedDateTime

sealed class PublishBookResponse
object InvalidCurrencyResponse : PublishBookResponse()
data class BookPublishedSuccessfullyResponse(
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
) : PublishBookResponse()