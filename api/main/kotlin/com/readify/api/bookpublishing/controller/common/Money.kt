package com.readify.api.bookpublishing.controller.common

import com.fasterxml.jackson.annotation.JsonProperty

data class HttpMoney(
    @JsonProperty("amount") val amount: Float,
    @JsonProperty("currency") val currency: String
)