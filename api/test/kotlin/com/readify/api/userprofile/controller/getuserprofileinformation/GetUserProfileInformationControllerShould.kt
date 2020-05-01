package com.readify.api.userprofile.controller.getuserprofileinformation

import com.ninjasquad.springmockk.MockkBean
import com.readify.ContractTest
import com.readify.api.userprofile.controller.common.UserInformationResponseMother
import com.readify.userprofile.application.getuserprofileinformation.GetUserProfileInformationRequest
import com.readify.userprofile.application.getuserprofileinformation.GetUserProfileInformationService
import com.readify.userprofile.application.getuserprofileinformation.UserFoundResponse
import com.readify.userprofile.application.getuserprofileinformation.UserNotFound
import io.mockk.every
import io.restassured.RestAssured
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Test
import java.util.UUID

class GetUserProfileInformationControllerShould : ContractTest() {
    @MockkBean
    private lateinit var service: GetUserProfileInformationService
    @Test
    fun `return 404 when requested user does not exists`() {
        val serviceRequest = GetUserProfileInformationRequest(userId)
        every { service.execute(serviceRequest) } returns UserNotFound

        RestAssured.given()
            .`when`()
            .get("/v1/users/$userId")
            .then()
            .statusCode(404)
    }

    @Test
    fun `return ok with requested user`() {
        val serviceRequest = GetUserProfileInformationRequest(userId)
        val response = UserFoundResponse(UserInformationResponseMother().existentUser(userId))
        every { service.execute(serviceRequest) } returns response

        RestAssured.given()
            .`when`()
            .get("/v1/users/$userId")
            .then()
            .statusCode(200)
            .body("id", equalTo(response.user.id))
            .body("username", equalTo(response.user.username))
            .body("email", equalTo(response.user.email))
            .body("fullName", equalTo(response.user.fullName))
            .body("image", equalTo(response.user.image))
            .body("website", equalTo(response.user.website))
    }

    companion object {
        private val userId = UUID.randomUUID().toString()
    }
}