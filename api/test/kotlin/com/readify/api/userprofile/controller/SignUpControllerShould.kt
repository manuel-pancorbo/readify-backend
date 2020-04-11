package com.readify.api.userprofile.controller

import com.ninjasquad.springmockk.MockkBean
import com.readify.ContractTest
import com.readify.userprofile.application.SignUpResponse
import com.readify.userprofile.application.SignUpService
import com.readify.userprofile.domain.user.EmailAlreadyRegisteredException
import com.readify.userprofile.domain.user.UsernameAlreadyRegisteredException
import io.mockk.every
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Test

class SignUpControllerShould : ContractTest() {
    @MockkBean(relaxed = true)
    private lateinit var signUpService: SignUpService

    @Test
    fun `returns http ok after create an user`() {
        every { signUpService.execute(any()) } returns SignUpResponse

        given()
            .`when`()
            .contentType("application/json")
            .and()
            .body(
                """{
                            "username": "manuel.pancorbo",
                            "email": "manuelpancorbo90@gmail.com",
                            "password": "dummy-password",
                            "fullName": "J.K. Rowling"
                        }"""
            )
            .post("/v1/users")
            .then()
            .statusCode(200)
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
                            "password": "dummy-password",
                            "fullName": "J.K. Rowling" 
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
                            "password": "dummy-password",
                            "fullName": "J.K. Rowling" 
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