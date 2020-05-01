package com.readify.api.userprofile.controller.common

import com.fasterxml.jackson.annotation.JsonProperty

data class UserInformationHttpResponse(
    @JsonProperty("id") val id: String,
    @JsonProperty("username") val username: String,
    @JsonProperty("email") val email: String,
    @JsonProperty("fullName") val fullName: String,
    @JsonProperty("image") val image: String?,
    @JsonProperty("website") val website: String?
)
