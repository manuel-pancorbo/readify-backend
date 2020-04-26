package com.readify.readerlibrary.application.service.getbook

import java.time.ZonedDateTime

sealed class GetBookResponse
object BookNotFound : GetBookResponse()
data class BookResponse(
    val id: String,
    val authorId: String,
    val title: String,
    val cover: String,
    val summary: String,
    val tags: List<String>,
    val priceAmount: Float,
    val priceCurrency: String,
    val completionPercentage: Int,
    val status: BookStatusResponse,
    val chapters: List<ChapterResponse>,
    val finishedAt: ZonedDateTime?
) : GetBookResponse()

enum class BookStatusResponse { IN_PROGRESS, FINISHED }
data class ChapterResponse(
    val id: String,
    val title: String,
    val priceAmount: Float,
    val priceCurrency: String,
    val order: Int,
    val excerpt: String?
)