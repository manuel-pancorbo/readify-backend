package com.readify.api.bookpublishing.controller.createchapter

import com.fasterxml.jackson.annotation.JsonProperty

data class PostBookChapterHttpRequest(
    @JsonProperty("title") val title: String,
    @JsonProperty("content") val content: String
)