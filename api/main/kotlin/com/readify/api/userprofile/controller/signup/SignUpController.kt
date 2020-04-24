package com.readify.api.userprofile.controller.signup

import com.fasterxml.jackson.annotation.JsonProperty
import com.readify.userprofile.application.signup.SignUpRequest
import com.readify.userprofile.application.signup.SignUpService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1")
class SignUpController(private val signUpService: SignUpService) {
    @PostMapping("/users")
    fun signUp(@RequestBody body: SignUpHttpRequest): ResponseEntity<Nothing> =
        signUpService.execute(
            SignUpRequest(body.username, body.email, body.password, body.fullName, body.image, body.website)
        )
            .let { ResponseEntity.ok().build() }
}

data class SignUpHttpRequest(
    @JsonProperty("username") val username: String,
    @JsonProperty("email") val email: String,
    @JsonProperty("password") val password: String,
    @JsonProperty("fullName") val fullName: String,
    @JsonProperty("image") val image: String?,
    @JsonProperty("website") val website: String?
)