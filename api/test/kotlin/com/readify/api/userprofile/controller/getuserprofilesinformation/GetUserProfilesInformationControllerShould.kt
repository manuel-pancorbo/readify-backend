package com.readify.api.userprofile.controller.getuserprofilesinformation

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import com.ninjasquad.springmockk.MockkBean
import com.readify.ContractTest
import com.readify.api.userprofile.controller.common.UserInformationResponseMother
import com.readify.userprofile.application.getuserprofilesinformation.GetUserProfilesInformationRequest
import com.readify.userprofile.application.getuserprofilesinformation.GetUserProfilesInformationResponse
import com.readify.userprofile.application.getuserprofilesinformation.GetUserProfilesInformationService
import io.mockk.every
import io.restassured.RestAssured
import org.junit.jupiter.api.Test
import java.util.UUID

class GetUserProfilesInformationControllerShould : ContractTest() {
    @MockkBean
    private lateinit var service: GetUserProfilesInformationService

    @Test
    fun `return ok with requested users`() {
        val serviceRequest = GetUserProfilesInformationRequest(listOf(userId, anotherUserId))
        val firstUser = UserInformationResponseMother().existentUser(userId)
        val secondUser = UserInformationResponseMother().existentUser(anotherUserId)
        val response = GetUserProfilesInformationResponse(listOf(firstUser, secondUser))
        every { service.execute(serviceRequest) } returns response

        val httpResponse = RestAssured.given()
            .`when`()
            .get("/v1/users/?ids=$userId,$anotherUserId")
            .then()
            .statusCode(200)
            .extract()
            .response()
            .jsonPath()

        assertThat(httpResponse.getList<String>("users")).hasSize(2)
        val firstHttpUser = httpResponse.getMap<String, String>("users[0]")
        val secondHttpUser = httpResponse.getMap<String, String>("users[1]")
        assertThat(firstHttpUser["id"]).isEqualTo(firstUser.id)
        assertThat(firstHttpUser["username"]).isEqualTo(firstUser.username)
        assertThat(firstHttpUser["email"]).isEqualTo(firstUser.email)
        assertThat(firstHttpUser["fullName"]).isEqualTo(firstUser.fullName)
        assertThat(firstHttpUser["image"]).isEqualTo(firstUser.image)
        assertThat(firstHttpUser["website"]).isEqualTo(firstUser.website)
        assertThat(secondHttpUser["id"]).isEqualTo(secondUser.id)
        assertThat(secondHttpUser["username"]).isEqualTo(secondUser.username)
        assertThat(secondHttpUser["email"]).isEqualTo(secondUser.email)
        assertThat(secondHttpUser["fullName"]).isEqualTo(secondUser.fullName)
        assertThat(secondHttpUser["image"]).isEqualTo(secondUser.image)
        assertThat(secondHttpUser["website"]).isEqualTo(secondUser.website)
    }

    companion object {
        private val userId = UUID.randomUUID().toString()
        private val anotherUserId = UUID.randomUUID().toString()
    }
}