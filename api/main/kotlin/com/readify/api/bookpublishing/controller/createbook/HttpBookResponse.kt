package com.readify.api.bookpublishing.controller.createbook

import com.fasterxml.jackson.annotation.JsonProperty

data class HttpBookResponse(
    @JsonProperty("id") val id: String,
    @JsonProperty("authorId") val authorId: String,
    @JsonProperty("title") val title: String,
    @JsonProperty("summary") val summary: String,
    @JsonProperty("cover") val cover: String,
    @JsonProperty("tags") val tags: List<String>
)