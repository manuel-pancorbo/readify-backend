package com.readify.api.authentication.controller

import com.fasterxml.jackson.annotation.JsonProperty
import com.readify.authentication.application.service.createaccesstoken.CreateAccessTokenRequest
import com.readify.authentication.application.service.createaccesstoken.CreateAccessTokenService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/auth")
class GenerateAccessTokenController(private val service: CreateAccessTokenService) {
    @PostMapping("/token")
    fun generateToken(@RequestBody body: GenerateAccessTokenHttpRequest): ResponseEntity<GenerateAccessTokenHttpResponse> =
        service.execute(CreateAccessTokenRequest(body.userIdentifier, body.password))
            .let { ResponseEntity.ok(GenerateAccessTokenHttpResponse(it.token)) }
}

data class GenerateAccessTokenHttpRequest(
    @JsonProperty("userIdentifier") val userIdentifier: String,
    @JsonProperty("password") val password: String
)

data class GenerateAccessTokenHttpResponse(@JsonProperty("token") val token: String)