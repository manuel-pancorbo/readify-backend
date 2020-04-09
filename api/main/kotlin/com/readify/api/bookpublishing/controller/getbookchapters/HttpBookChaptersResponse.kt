package com.readify.api.bookpublishing.controller.getbookchapters

import com.fasterxml.jackson.annotation.JsonProperty
import com.readify.api.bookpublishing.controller.common.HttpMoney
import java.time.ZonedDateTime

data class HttpBookChaptersResponse(
    @JsonProperty("chapters") val chapters: List<HttpChapterSummary>
)
data class HttpChapterSummary(
    @JsonProperty("id") val id: String,
    @JsonProperty("title") val title: String,
    @JsonProperty("modifiedAt") val modifiedAt: ZonedDateTime,
    @JsonProperty("book") val book: String,
    @JsonProperty("author") val author: String,
    @JsonProperty("status") val status: String,
    @JsonProperty("price") val price: HttpMoney,
    @JsonProperty("order") val order: Int,
    @JsonProperty("excerpt") val excerpt: String?
)