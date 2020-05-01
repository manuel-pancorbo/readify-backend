package com.readify.api.userprofile.controller.getuserinformation

import com.readify.api.userprofile.controller.common.UserInformationHttpResponse
import com.readify.userprofile.application.getuserprofileinformation.GetUserProfileInformationRequest
import com.readify.userprofile.application.getuserprofileinformation.GetUserProfileInformationResponse
import com.readify.userprofile.application.getuserprofileinformation.GetUserProfileInformationService
import com.readify.userprofile.application.getuserprofileinformation.UserFoundResponse
import com.readify.userprofile.application.getuserprofileinformation.UserNotFound
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1")
class GetUserInformationController(private val service: GetUserProfileInformationService) {
    @GetMapping("/users/{userId}")
    fun signUp(@PathVariable userId: String): ResponseEntity<UserInformationHttpResponse> =
        service.execute(
            GetUserProfileInformationRequest(userId)
        )
            .toHttp()
}

private fun GetUserProfileInformationResponse.toHttp() =
    when (this) {
        UserNotFound -> ResponseEntity.notFound().build()
        is UserFoundResponse -> ResponseEntity.ok(
            UserInformationHttpResponse(user.id, user.username, user.email, user.fullName, user.image, user.website)
        )
    }