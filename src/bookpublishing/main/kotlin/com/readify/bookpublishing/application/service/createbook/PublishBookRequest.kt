package com.readify.bookpublishing.application.service.createbook

data class PublishBookRequest(
    val authorId: String,
    val title: String,
    val summary: String,
    val cover: String,
    val tags: List<String>,
    val priceAmount: Float,
    val priceCurrency: String
)