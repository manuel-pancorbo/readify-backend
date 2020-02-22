package com.readify.shared.infrastructure.controller.error

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
data class HttpErrorResponse(
    @JsonProperty("code") val code: String,
    @JsonProperty("message") val message: String,
    @JsonProperty("field") val field: String? = null
)