package com.readify.userprofile.application.getuserprofileinformation

sealed class GetUserProfileInformationResponse
object UserNotFound : GetUserProfileInformationResponse()
data class UserInformationResponse(
    val id: String, val username: String, val email: String, val fullName: String, val image: String?,
    val website: String?
) : GetUserProfileInformationResponse()
