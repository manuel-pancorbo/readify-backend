package com.readify.api.authentication.controller

import com.ninjasquad.springmockk.MockkBean
import com.readify.ContractTest
import com.readify.authentication.application.service.createaccesstoken.CreateAccessTokenResponse
import com.readify.authentication.application.service.createaccesstoken.CreateAccessTokenService
import com.readify.authentication.domain.usercredentials.InvalidUserCredentialsException
import io.mockk.every
import io.restassured.RestAssured
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.Test

class GenerateAccessTokenControllerShould : ContractTest() {
    @MockkBean(relaxed = true)
    private lateinit var createAccessTokenService: CreateAccessTokenService

    @Test
    fun `returns http ok when token has been generated successfully`() {
        every { createAccessTokenService.execute(any()) } returns CreateAccessTokenResponse("anytoken")

        RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .body(
                """{
                            "userIdentifier": "manuel.pancorbo",
                            "password": "dummy-password"
                        }"""
            )
            .post("/v1/auth/token")
            .then()
            .statusCode(200)
            .body("token", CoreMatchers.equalTo("anytoken"))
    }

    @Test
    fun `returns http 401 unauthorized when provided credentials are not valid`() {
        every { createAccessTokenService.execute(any()) } throws InvalidUserCredentialsException()

        RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .body(
                """{
                            "userIdentifier": "manuel.pancorbo",
                            "password": "dummy-password"
                        }"""
            )
            .post("/v1/auth/token")
            .then()
            .statusCode(401)
            .body("message", CoreMatchers.containsStringIgnoringCase("invalid credentials"))
            .body("code", CoreMatchers.equalTo("auth.invalid-credentials"))
    }
}