package com.readify.api.bookpublishing.controller.createbook

import com.fasterxml.jackson.annotation.JsonProperty
import com.readify.api.bookpublishing.controller.common.HttpMoney
import java.time.ZonedDateTime

data class HttpBookResponse(
    @JsonProperty("id") val id: String,
    @JsonProperty("authorId") val authorId: String,
    @JsonProperty("title") val title: String,
    @JsonProperty("summary") val summary: String,
    @JsonProperty("cover") val cover: String,
    @JsonProperty("tags") val tags: List<String>,
    @JsonProperty("price") val price: HttpMoney,
    @JsonProperty("status") val status: String,
    @JsonProperty("visibility") val visibility: String,
    @JsonProperty("completionPercentage") val completionPercentage: Int,
    @JsonProperty("finishedAt") val finishedAt: ZonedDateTime?
)
