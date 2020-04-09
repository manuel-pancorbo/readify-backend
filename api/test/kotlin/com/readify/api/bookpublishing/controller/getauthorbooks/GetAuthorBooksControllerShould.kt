package com.readify.api.bookpublishing.controller.getauthorbooks

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import com.ninjasquad.springmockk.MockkBean
import com.readify.ContractTest
import com.readify.authentication.application.service.verifyaccesstoken.VerifyAccessTokenRequest
import com.readify.authentication.application.service.verifyaccesstoken.VerifyAccessTokenResponse
import com.readify.authentication.application.service.verifyaccesstoken.VerifyAccessTokenService
import com.readify.bookpublishing.application.service.getauthorbooks.GetAuthorBooksRequest
import com.readify.bookpublishing.application.service.getauthorbooks.GetAuthorBooksService
import io.mockk.every
import io.restassured.RestAssured
import org.junit.jupiter.api.Test
import java.time.format.DateTimeFormatter

class GetAuthorBooksControllerShould : ContractTest() {
    @MockkBean
    private lateinit var service: GetAuthorBooksService

    @MockkBean(relaxed = true)
    private lateinit var verifyAccessTokenService: VerifyAccessTokenService

    @Test
    fun `returns unauthorized when user is not authenticated`() {
        RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .get("/v1/books")
            .then()
            .statusCode(401)
    }

    @Test
    fun `return ok when chapter is created successfully`() {
        val serviceResponse = GetAuthorBooksResponseMother().singleBookResponse(authorId)
        val serviceRequest = GetAuthorBooksRequest(authorId)
        every { verifyAccessTokenService.execute(VerifyAccessTokenRequest("anytoken")) }
            .returns(VerifyAccessTokenResponse(authorId, "jkrowling", "jkrowling@gmail.com"))
        every { service.execute(serviceRequest) } returns serviceResponse

        val httpResponse = RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .header("Authorization", "Bearer anytoken")
            .and()
            .get("/v1/books")
            .then()
            .statusCode(200)
            .extract()
            .response()
            .jsonPath()

        assertThat(httpResponse.getList<String>("books")).hasSize(1)
        val httpBookResponse = httpResponse.getMap<String, String>("books[0]")
        val httpPrice = httpResponse.getMap<String, String>("books[0].price")
        val tags = httpResponse.getList<String>("books[0].tags")
        assertThat(httpBookResponse["id"]).isEqualTo(serviceResponse.books[0].bookId)
        assertThat(httpBookResponse["authorId"]).isEqualTo(serviceResponse.books[0].authorId)
        assertThat(httpBookResponse["cover"]).isEqualTo(serviceResponse.books[0].cover)
        assertThat(httpBookResponse["title"]).isEqualTo(serviceResponse.books[0].title)
        assertThat(httpBookResponse["summary"]).isEqualTo(serviceResponse.books[0].summary)
        assertThat(httpBookResponse["status"]).isEqualTo(serviceResponse.books[0].status.toString().toLowerCase())
        assertThat(httpBookResponse["visibility"]).isEqualTo(serviceResponse.books[0].visibility.toString().toLowerCase())
        assertThat(httpBookResponse["completionPercentage"]).isEqualTo(serviceResponse.books[0].completionPercentage)
        assertThat(httpPrice["amount"]).isEqualTo(serviceResponse.books[0].priceAmount)
        assertThat(httpPrice["currency"]).isEqualTo(serviceResponse.books[0].priceCurrency)
        assertThat(tags).isEqualTo(serviceResponse.books[0].tags)
        assertThat(httpBookResponse["finishedAt"]).isEqualTo(serviceResponse.books[0].finishedAt?.format(
            DateTimeFormatter.ISO_OFFSET_DATE_TIME))
    }

    companion object {
        private const val authorId = "any-author-id"
    }
}