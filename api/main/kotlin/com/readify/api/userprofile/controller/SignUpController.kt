package com.readify.api.userprofile.controller

import com.fasterxml.jackson.annotation.JsonProperty
import com.readify.userprofile.application.SignUpRequest
import com.readify.userprofile.application.SignUpService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1")
class SignUpController(private val signUpService: SignUpService) {
    @PostMapping("/users")
    fun signUp(@RequestBody body: SignUpHttpRequest): ResponseEntity<SignUpHttpResponse> =
        signUpService.execute(SignUpRequest(body.username, body.email, body.password))
            .let { ResponseEntity.ok(
                SignUpHttpResponse(it.id, it.username, it.email)
            ) }
}

data class SignUpHttpRequest(
    @JsonProperty("username") val username: String = "",
    @JsonProperty("email") val email: String = "",
    @JsonProperty("password") val password: String = ""
)

data class SignUpHttpResponse(
    @JsonProperty("id") val userId: String,
    @JsonProperty("username") val username: String,
    @JsonProperty("email") val email: String
)