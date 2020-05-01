package com.readify.userprofile.application.getuserprofileinformation

import com.readify.userprofile.application.common.UserInformationResponse

sealed class GetUserProfileInformationResponse
object UserNotFound : GetUserProfileInformationResponse()
data class UserFoundResponse(val user: UserInformationResponse) : GetUserProfileInformationResponse()
