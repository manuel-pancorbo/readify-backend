package com.readify.bookpublishing.application.service.updatechapter

data class UpdateBookChapterRequest(
    val authorId: String,
    val bookId: String,
    val chapterId: String,
    val status: String? = null,
    val title: String? = null,
    val content: String? = null,
    val order: Int? = null,
    val excerpt: String? = null,
    val priceAmount: Float? = null,
    val priceCurrency: String? = null
)
