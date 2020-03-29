package com.readify.bookpublishing.application.service.updatechapter

import com.readify.bookpublishing.application.service.createchapter.ChapterStatus
import java.time.ZonedDateTime

sealed class UpdateBookChapterResponse
object BookChapterNotFoundResponse: UpdateBookChapterResponse()
object BookNotBelongToAuthorResponse: UpdateBookChapterResponse()
object InvalidChapterStatusResponse: UpdateBookChapterResponse()
data class BookChapterUpdatedResponse(
    val id: String,
    val title: String,
    val content: String,
    val modifiedAt: ZonedDateTime,
    val authorId: String,
    val bookId: String,
    val status: ChapterStatus,
    val priceAmount: Float,
    val priceCurrency: String,
    val order: Int,
    val excerpt: String?
): UpdateBookChapterResponse()