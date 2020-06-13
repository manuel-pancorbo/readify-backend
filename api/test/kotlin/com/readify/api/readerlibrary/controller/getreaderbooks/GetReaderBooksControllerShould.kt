package com.readify.api.readerlibrary.controller.getreaderbooks

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import com.ninjasquad.springmockk.MockkBean
import com.readify.ContractTest
import com.readify.authentication.application.service.verifyaccesstoken.VerifyAccessTokenRequest
import com.readify.authentication.application.service.verifyaccesstoken.VerifyAccessTokenResponse
import com.readify.authentication.application.service.verifyaccesstoken.VerifyAccessTokenService
import com.readify.readerlibrary.application.service.getreaderbooks.GetReaderBooksRequest
import com.readify.readerlibrary.application.service.getreaderbooks.GetReaderBooksService
import com.readify.readerlibrary.application.service.getreaderbooks.ReaderBooksResponse
import com.readify.readerlibrary.application.service.getreaderbooks.RequesterAndRequestedReaderAreDifferent
import io.mockk.every
import io.restassured.RestAssured
import org.junit.jupiter.api.Test
import java.util.UUID

class GetReaderBooksControllerShould : ContractTest() {
    @MockkBean
    private lateinit var service: GetReaderBooksService

    @MockkBean(relaxed = true)
    private lateinit var verifyAccessTokenService: VerifyAccessTokenService

    @Test
    fun `returns unauthorized when user is not authenticated`() {
        RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .get("/v1/readers/$readerId/books")
            .then()
            .statusCode(401)
    }

    @Test
    fun `return 404 when reader is requesting books of another reader`() {
        val serviceRequest = GetReaderBooksRequest(anotherReaderId, readerId)
        every { verifyAccessTokenService.execute(VerifyAccessTokenRequest("anytoken")) }
            .returns(VerifyAccessTokenResponse(anotherReaderId, "jkrowling", "jkrowling@gmail.com"))
        every { service.execute(serviceRequest) } returns RequesterAndRequestedReaderAreDifferent

        RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .header("Authorization", "Bearer anytoken")
            .and()
            .get("/v1/readers/$readerId/books")
            .then()
            .statusCode(404)
    }

    @Test
    fun `return ok with the list of books`() {
        val serviceRequest = GetReaderBooksRequest(readerId, readerId)
        val serviceResponse = ReaderBooksResponse(
            listOf(
                BookResponseMother().createFinishedBook(wholeBookId),
                BookResponseMother().createFinishedBook(partialBookId)
            )
        )
        every { verifyAccessTokenService.execute(VerifyAccessTokenRequest("anytoken")) }
            .returns(VerifyAccessTokenResponse(readerId, "jkrowling", "jkrowling@gmail.com"))
        every { service.execute(serviceRequest) } returns serviceResponse

        val httpResponse = RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .header("Authorization", "Bearer anytoken")
            .and()
            .get("/v1/readers/$readerId/books")
            .then()
            .statusCode(200)
            .extract()
            .response()
            .jsonPath()

        assertThat(httpResponse.getList<String>("books")).hasSize(2)
        val httpWholeBook = httpResponse.getMap<String, String>("books[0]")
        assertThat(httpWholeBook["id"]).isEqualTo(wholeBookId)
        val httpPartialBook = httpResponse.getMap<String, String>("books[1]")
        assertThat(httpPartialBook["id"]).isEqualTo(partialBookId)
    }

    companion object {
        private val readerId = UUID.randomUUID().toString()
        private val anotherReaderId = UUID.randomUUID().toString()
        private val wholeBookId = UUID.randomUUID().toString()
        private val partialBookId = UUID.randomUUID().toString()
    }
}