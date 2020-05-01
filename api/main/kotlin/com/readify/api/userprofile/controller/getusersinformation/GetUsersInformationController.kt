package com.readify.api.userprofile.controller.getusersinformation

import com.fasterxml.jackson.annotation.JsonProperty
import com.readify.api.userprofile.controller.common.UserInformationHttpResponse
import com.readify.userprofile.application.common.UserInformationResponse
import com.readify.userprofile.application.getuserprofilesinformation.GetUserProfilesInformationRequest
import com.readify.userprofile.application.getuserprofilesinformation.GetUserProfilesInformationResponse
import com.readify.userprofile.application.getuserprofilesinformation.GetUserProfilesInformationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1")
class GetUsersInformationController(private val service: GetUserProfilesInformationService) {
    @GetMapping("/users")
    fun signUp(@RequestParam("ids") ids: String): ResponseEntity<GetUsersInformationHttpResponse> =
        service.execute(
            GetUserProfilesInformationRequest(ids.split(","))
        )
            .toHttp()
}

private fun GetUserProfilesInformationResponse.toHttp() =
    ResponseEntity.ok(GetUsersInformationHttpResponse(users.map { it.toHttpResponse() }))

private fun UserInformationResponse.toHttpResponse(): UserInformationHttpResponse {
    return UserInformationHttpResponse(id, username, email, fullName, image, website)
}

data class GetUsersInformationHttpResponse(@JsonProperty("users") val users: List<UserInformationHttpResponse>)