package com.readify.bookpublishing.application.service.getbookchapters

import com.readify.bookpublishing.application.service.common.ChapterStatus
import java.time.ZonedDateTime

sealed class GetBookChaptersResponse
object BookNotFoundResponse : GetBookChaptersResponse()
data class BookChaptersResponse(val chapters: List<ChapterSummaryResponse>) : GetBookChaptersResponse()
data class ChapterSummaryResponse(
    val id: String,
    val title: String,
    val modifiedAt: ZonedDateTime,
    val authorId: String,
    val bookId: String,
    val status: ChapterStatus,
    val priceAmount: Float,
    val priceCurrency: String,
    val order: Int,
    val excerpt: String?
)