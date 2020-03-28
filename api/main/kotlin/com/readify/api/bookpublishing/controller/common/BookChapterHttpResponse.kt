package com.readify.api.bookpublishing.controller.common

import com.fasterxml.jackson.annotation.JsonProperty
import com.readify.api.bookpublishing.controller.common.HttpMoney
import java.time.ZonedDateTime

data class BookChapterHttpResponse(
    @JsonProperty("id") val id: String,
    @JsonProperty("title") val title: String,
    @JsonProperty("content") val content: String,
    @JsonProperty("modifiedAt") val modifiedAt: ZonedDateTime,
    @JsonProperty("book") val book: String,
    @JsonProperty("author") val author: String,
    @JsonProperty("status") val status: String,
    @JsonProperty("price") val price: HttpMoney
)