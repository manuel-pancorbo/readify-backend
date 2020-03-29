package com.readify.bookpublishing.application.service.createchapter

data class CreateChapterRequest(val title: String, val content: String, val authorId: String, val bookId: String,
                                val priceAmount: Float, val priceCurrency: String, val order: Int, val excerpt: String?)