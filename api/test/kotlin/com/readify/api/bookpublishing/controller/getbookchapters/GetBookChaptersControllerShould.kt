package com.readify.api.bookpublishing.controller.getbookchapters

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import com.ninjasquad.springmockk.MockkBean
import com.readify.ContractTest
import com.readify.authentication.application.service.verifyaccesstoken.VerifyAccessTokenRequest
import com.readify.authentication.application.service.verifyaccesstoken.VerifyAccessTokenResponse
import com.readify.authentication.application.service.verifyaccesstoken.VerifyAccessTokenService
import com.readify.bookpublishing.application.service.getbookchapters.BookNotFoundResponse
import com.readify.bookpublishing.application.service.getbookchapters.GetBookChaptersRequest
import com.readify.bookpublishing.application.service.getbookchapters.GetBookChaptersService
import io.mockk.every
import io.restassured.RestAssured
import org.junit.jupiter.api.Test
import java.time.format.DateTimeFormatter
import java.util.UUID

class GetBookChaptersControllerShould : ContractTest() {
    @MockkBean
    private lateinit var getBookChaptersService: GetBookChaptersService

    @MockkBean(relaxed = true)
    private lateinit var verifyAccessTokenService: VerifyAccessTokenService

    @Test
    fun `returns unauthorized when user is not authenticated`() {
        RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .get("/v1/authors/$authorId/books/$bookId/chapters")
            .then()
            .statusCode(401)
    }

    @Test
    fun `return 404 when the requested book does not exists`() {
        val serviceRequest = GetBookChaptersRequest(authorId, bookId)
        every { verifyAccessTokenService.execute(VerifyAccessTokenRequest("anytoken")) }
            .returns(VerifyAccessTokenResponse(authorId, "jkrowling", "jkrowling@gmail.com"))
        every { getBookChaptersService.execute(serviceRequest) } returns BookNotFoundResponse

        RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .header("Authorization", "Bearer anytoken")
            .and()
            .get("/v1/authors/$authorId/books/$bookId/chapters")
            .then()
            .statusCode(404)
    }

    @Test
    fun `return 404 when the requester is not the resource author`() {
        every { verifyAccessTokenService.execute(VerifyAccessTokenRequest("anytoken")) }
            .returns(VerifyAccessTokenResponse(anotherAuthorId, "jkrowling", "jkrowling@gmail.com"))

        RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .header("Authorization", "Bearer anytoken")
            .and()
            .get("/v1/authors/$authorId/books/$bookId/chapters")
            .then()
            .statusCode(404)
    }

    @Test
    fun `return ok when chapter is created successfully`() {
        val serviceRequest = GetBookChaptersRequest(authorId, bookId)
        val serviceResponse = GetBookChaptersResponseMother().singleChapterList(authorId, bookId)
        every { verifyAccessTokenService.execute(VerifyAccessTokenRequest("anytoken")) }
            .returns(VerifyAccessTokenResponse(authorId, "jkrowling", "jkrowling@gmail.com"))
        every { getBookChaptersService.execute(serviceRequest) } returns serviceResponse

        val httpResponse = RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .header("Authorization", "Bearer anytoken")
            .and()
            .get("/v1/authors/$authorId/books/$bookId/chapters")
            .then()
            .statusCode(200)
            .extract()
            .response()
            .jsonPath()

        assertThat(httpResponse.getList<String>("chapters")).hasSize(1)
        val httpChapterResponse = httpResponse.getMap<String, String>("chapters[0]")
        val httpPrice = httpResponse.getMap<String, String>("chapters[0].price")
        assertThat(httpChapterResponse["id"]).isEqualTo(serviceResponse.chapters[0].id)
        assertThat(httpChapterResponse["book"]).isEqualTo(serviceResponse.chapters[0].bookId)
        assertThat(httpChapterResponse["author"]).isEqualTo(serviceResponse.chapters[0].authorId)
        assertThat(httpChapterResponse["title"]).isEqualTo(serviceResponse.chapters[0].title)
        assertThat(httpChapterResponse["excerpt"]).isEqualTo(serviceResponse.chapters[0].excerpt)
        assertThat(httpPrice["amount"]).isEqualTo(serviceResponse.chapters[0].priceAmount)
        assertThat(httpPrice["currency"]).isEqualTo(serviceResponse.chapters[0].priceCurrency)
        assertThat(httpChapterResponse["status"]).isEqualTo(serviceResponse.chapters[0].status.toString().toLowerCase())
        assertThat(httpChapterResponse["order"]).isEqualTo(serviceResponse.chapters[0].order)
        assertThat(httpChapterResponse["modifiedAt"])
            .isEqualTo(serviceResponse.chapters[0].modifiedAt.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
    }

    companion object {
        private val bookId = UUID.randomUUID().toString()
        private val authorId = UUID.randomUUID().toString()
        private val anotherAuthorId = UUID.randomUUID().toString()
    }
}