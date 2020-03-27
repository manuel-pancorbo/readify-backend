package com.readify.api.bookpublishing.controller.createchapter

import com.fasterxml.jackson.annotation.JsonProperty
import com.readify.api.bookpublishing.controller.common.HttpMoney

data class PostBookChapterHttpRequest(
    @JsonProperty("title") val title: String,
    @JsonProperty("content") val content: String,
    @JsonProperty("price") val price: HttpMoney
)