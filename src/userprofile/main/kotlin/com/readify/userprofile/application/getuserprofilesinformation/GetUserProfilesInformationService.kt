package com.readify.userprofile.application.getuserprofilesinformation

import com.readify.userprofile.application.common.UserInformationResponse
import com.readify.userprofile.domain.user.User
import com.readify.userprofile.domain.user.UserId
import com.readify.userprofile.domain.user.UserRepository

class GetUserProfilesInformationService(private val userRepository: UserRepository) {
    fun execute(request: GetUserProfilesInformationRequest): GetUserProfilesInformationResponse {
        return userRepository.findByIds(request.ids.map { UserId(it) })
            .map { it.toResponse() }
            .let { GetUserProfilesInformationResponse(it) }
    }
}

data class GetUserProfilesInformationRequest(val ids: List<String>)
data class GetUserProfilesInformationResponse(val users: List<UserInformationResponse>)

private fun User.toResponse() =
    UserInformationResponse(
        id.value,
        username.value,
        email.value,
        fullName.value,
        image?.value,
        website?.value
    )