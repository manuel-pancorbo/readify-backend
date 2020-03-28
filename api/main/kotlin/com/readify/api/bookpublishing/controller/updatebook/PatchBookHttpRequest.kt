package com.readify.api.bookpublishing.controller.updatebook

import com.fasterxml.jackson.annotation.JsonProperty

data class PatchBookHttpRequest(
    @JsonProperty("completionPercentage") val completionPercentage: Int
)