package com.readify.api.bookpublishing.controller.createchapter

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.ZonedDateTime

data class PostBookChapterHttpResponse(
    @JsonProperty("id") val id: String,
    @JsonProperty("title") val title: String,
    @JsonProperty("content") val content: String,
    @JsonProperty("modifiedAt") val modifiedAt: ZonedDateTime,
    @JsonProperty("book") val book: String,
    @JsonProperty("author") val author: String,
    @JsonProperty("status") val status: String
)