package com.readify.search.infrastructure.domain.book

import java.time.ZonedDateTime

data class ElasticSearchBook(
    val bookId: String,
    val authorId: String,
    val title: String,
    val cover: String,
    val summary: String,
    val tags: List<String>,
    val price: ElasticSearchMoney,
    val completionPercentage: Int,
    val status: String,
    val finishedAt: String?
)

data class ElasticSearchMoney(val amount: Float, val currency: String)