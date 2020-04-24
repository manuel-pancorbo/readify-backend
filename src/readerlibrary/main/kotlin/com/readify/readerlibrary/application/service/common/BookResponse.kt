package com.readify.readerlibrary.application.service.common

import java.time.ZonedDateTime

data class BookResponse(
    val id: String,
    val author: String,
    val title: String,
    val cover: String,
    val summary: String,
    val tags: List<String>,
    val priceAmount: Float,
    val priceCurrency: String,
    val completionPercentage: Int,
    val status: StatusResponse,
    val finishedAt: ZonedDateTime?
)

enum class StatusResponse { IN_PROGRESS, FINISHED }