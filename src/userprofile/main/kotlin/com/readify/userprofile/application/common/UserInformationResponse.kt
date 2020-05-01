package com.readify.userprofile.application.common

data class UserInformationResponse(
    val id: String, val username: String, val email: String, val fullName: String, val image: String?,
    val website: String?
)