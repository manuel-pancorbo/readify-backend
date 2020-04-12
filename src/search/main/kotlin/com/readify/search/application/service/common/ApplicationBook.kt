package com.readify.search.application.service.common

import java.time.ZonedDateTime

data class ApplicationBook(
    val bookId: String,
    val authorId: String,
    val title: String,
    val cover: String,
    val summary: String,
    val tags: List<String>,
    val priceAmount: Float,
    val priceCurrency: String,
    val completionPercentage: Int,
    val status: ApplicationBookStatus,
    val finishedAt: ZonedDateTime?
)

enum class ApplicationBookStatus { IN_PROGRESS, FINISHED }