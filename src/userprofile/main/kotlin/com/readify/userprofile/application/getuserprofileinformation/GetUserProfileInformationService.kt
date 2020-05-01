package com.readify.userprofile.application.getuserprofileinformation

import com.readify.userprofile.application.common.UserInformationResponse
import com.readify.userprofile.domain.user.User
import com.readify.userprofile.domain.user.UserId
import com.readify.userprofile.domain.user.UserRepository

class GetUserProfileInformationService(private val repository: UserRepository) {
    fun execute(request: GetUserProfileInformationRequest) =
        repository.findById(UserId(request.userId))
            ?.toResponse()
            ?: UserNotFound
}

private fun User.toResponse() =
    UserFoundResponse(
        UserInformationResponse(
            id.value,
            username.value,
            email.value,
            fullName.value,
            image?.value,
            website?.value
        )
    )
