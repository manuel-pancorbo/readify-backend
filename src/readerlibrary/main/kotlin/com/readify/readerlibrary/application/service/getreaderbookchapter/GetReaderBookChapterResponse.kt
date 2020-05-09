package com.readify.readerlibrary.application.service.getreaderbookchapter

import java.time.ZonedDateTime

sealed class GetReaderBookChapterResponse
object BookChapterDoesNotBelongToReader : GetReaderBookChapterResponse()
data class FullChapterResponse(
    val id: String,
    val title: String,
    val content: String,
    val priceAmount: Float,
    val priceCurrency: String,
    val authorId: String,
    val bookId: String,
    val modifiedAt: ZonedDateTime,
    val order: Int,
    val excerpt: String?,
    val publishedAt: ZonedDateTime
) : GetReaderBookChapterResponse()