package com.readify.userprofile.application.getuserprofileinformation

class GetUserProfileInformationService {
    fun execute(request: GetUserProfileInformationRequest): GetUserProfileInformationResponse {
        TODO("Not yet implemented")
    }

}

data class GetUserProfileInformationRequest(val userId: String)
sealed class GetUserProfileInformationResponse
object UserNotFound : GetUserProfileInformationResponse()
data class UserInformationResponse(
    val id: String, val username: String, val email: String, val fullName: String, val image: String?,
    val website: String?
) : GetUserProfileInformationResponse()