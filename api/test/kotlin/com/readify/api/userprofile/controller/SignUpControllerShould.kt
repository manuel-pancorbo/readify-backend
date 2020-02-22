package com.readify.api.userprofile.controller

import com.ninjasquad.springmockk.MockkBean
import com.readify.api.Application
import com.readify.userprofile.application.SignUpResponse
import com.readify.userprofile.application.SignUpService
import com.readify.userprofile.domain.user.EmailAlreadyRegisteredException
import com.readify.userprofile.domain.user.UsernameAlreadyRegisteredException
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import java.util.UUID

@ExtendWith(MockKExtension::class)
@SpringBootTest(classes = [Application::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SignUpControllerShould {
    @MockkBean(relaxed = true)
    private lateinit var signUpService: SignUpService

    @LocalServerPort
    private val port = 0

    @BeforeEach
    fun setUp() {
        RestAssured.port = port
    }

    @Test
    fun `returns http ok after create an user`() {
        val anyId = UUID.randomUUID().toString()
        val response = SignUpResponse(anyId, "manuel.pancorbo", "manuelpancorbo90@gmail.com")
        every { signUpService.execute(any()) } returns response

        given()
            .`when`()
            .contentType("application/json")
            .and()
            .body(
                """{
                            "username": "manuel.pancorbo",
                            "email": "manuelpancorbo90@gmail.com",
                            "password": "dummy-password"
                        }"""
            )
            .post("/v1/users")
            .then()
            .statusCode(200)
            .body("userId", notNullValue())
            .body("username", equalTo("manuel.pancorbo"))
            .body("email", equalTo("manuelpancorbo90@gmail.com"))
    }

    @Test
    fun `returns http 409 conflict after when requested email already exists`() {
        every { signUpService.execute(any()) } throws EmailAlreadyRegisteredException("manuel.pancorbo@gmail.com")

        given()
            .`when`()
            .contentType("application/json")
            .and()
            .body(
                """{
                            "username": "manuel.pancorbo",
                            "email": "manuel.pancorbo@gmail.com",
                            "password": "dummy-password"
                        }"""
            )
            .post("/v1/users")
            .then()
            .statusCode(409)
            .body("message", containsString("manuel.pancorbo@gmail.com"))
            .body("code", equalTo("signup.email"))
            .body("field", equalTo("email"))
    }

    @Test
    fun `returns http 409 conflict after when requested username already exists`() {
        every { signUpService.execute(any()) } throws UsernameAlreadyRegisteredException("manu")

        given()
            .`when`()
            .contentType("application/json")
            .and()
            .body(
                """{
                            "username": "manuel.pancorbo",
                            "email": "manuel.pancorbo@gmail.com",
                            "password": "dummy-password"
                        }"""
            )
            .post("/v1/users")
            .then()
            .statusCode(409)
            .body("message", containsString("manu"))
            .body("code", equalTo("signup.username"))
            .body("field", equalTo("username"))
    }
}