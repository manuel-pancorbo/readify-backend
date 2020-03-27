package com.readify.bookpublishing.application.service.createchapter

import java.time.ZonedDateTime

sealed class CreateChapterResponse
object BookNotFoundResponse : CreateChapterResponse()
object BookNotBelongToAuthorResponse : CreateChapterResponse()
object InvalidCurrencyResponse : CreateChapterResponse()
data class ChapterCreatedResponse(
    val id: String,
    val title: String,
    val content: String,
    val modifiedAt: ZonedDateTime,
    val authorId: String,
    val bookId: String,
    val status: ChapterStatus,
    val priceAmount: Float,
    val priceCurrency: String
) : CreateChapterResponse()

enum class ChapterStatus { DRAFT, PUBLISHED }