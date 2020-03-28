package com.readify.api.bookpublishing.controller.updatebook

import com.fasterxml.jackson.annotation.JsonProperty
import com.readify.api.bookpublishing.controller.common.HttpMoney

data class PatchBookHttpRequest(
    @JsonProperty("title") val title: String?,
    @JsonProperty("summary") val summary: String?,
    @JsonProperty("cover") val cover: String?,
    @JsonProperty("tags") val tags: List<String>?,
    @JsonProperty("price") val price: HttpMoney?,
    @JsonProperty("visibility") val visibility: String?,
    @JsonProperty("completionPercentage") val completionPercentage: Int?
)