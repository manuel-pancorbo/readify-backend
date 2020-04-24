package com.readify.api.userprofile.controller.getuserinformation

import com.fasterxml.jackson.annotation.JsonProperty
import com.readify.userprofile.application.getuserprofileinformation.GetUserProfileInformationRequest
import com.readify.userprofile.application.getuserprofileinformation.GetUserProfileInformationResponse
import com.readify.userprofile.application.getuserprofileinformation.GetUserProfileInformationService
import com.readify.userprofile.application.getuserprofileinformation.UserInformationResponse
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
        is UserInformationResponse -> ResponseEntity.ok(
            UserInformationHttpResponse(id, username, email, fullName, image, website)
        )
    }

data class UserInformationHttpResponse(
    @JsonProperty("id") val id: String,
    @JsonProperty("username") val username: String,
    @JsonProperty("email") val email: String,
    @JsonProperty("fullName") val fullName: String,
    @JsonProperty("image") val image: String?,
    @JsonProperty("website") val website: String?
)
