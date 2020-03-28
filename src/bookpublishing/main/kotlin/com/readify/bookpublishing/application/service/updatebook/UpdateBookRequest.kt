package com.readify.bookpublishing.application.service.updatebook

import com.readify.bookpublishing.application.service.common.BookVisibility

data class UpdateBookRequest(
    val authorId: String,
    val bookId: String,
    val title: String? = null,
    val summary: String? = null,
    val cover: String? = null,
    val tags: List<String> = emptyList(),
    val priceAmount: Float? = null,
    val priceCurrency: String? = null,
    val visibility: BookVisibility? = null,
    val completionPercentage: Int? = null
)