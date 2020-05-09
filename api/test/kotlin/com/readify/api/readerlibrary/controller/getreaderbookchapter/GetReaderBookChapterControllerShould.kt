package com.readify.api.readerlibrary.controller.getreaderbookchapter

import com.ninjasquad.springmockk.MockkBean
import com.readify.ContractTest
import com.readify.api.readerlibrary.controller.common.FullChapterResponseMother
import com.readify.authentication.application.service.verifyaccesstoken.VerifyAccessTokenRequest
import com.readify.authentication.application.service.verifyaccesstoken.VerifyAccessTokenResponse
import com.readify.authentication.application.service.verifyaccesstoken.VerifyAccessTokenService
import com.readify.readerlibrary.application.service.getreaderbookchapter.BookChapterDoesNotBelongToReader
import com.readify.readerlibrary.application.service.getreaderbookchapter.GetReaderBookChapterRequest
import com.readify.readerlibrary.application.service.getreaderbookchapter.GetReaderBookChapterService
import io.mockk.every
import io.restassured.RestAssured
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import java.time.format.DateTimeFormatter
import java.util.UUID

class GetReaderBookChapterControllerShould : ContractTest() {
    @MockkBean
    private lateinit var service: GetReaderBookChapterService

    @MockkBean(relaxed = true)
    private lateinit var verifyAccessTokenService: VerifyAccessTokenService

    @Test
    fun `returns unauthorized when user is not authenticated`() {
        RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .get("/v1/readers/$readerId/books/$bookId/chapters/$chapterId")
            .then()
            .statusCode(401)
    }

    @Test
    fun `return 404 when book chapter does not belongs to reader`() {
        val serviceRequest = GetReaderBookChapterRequest(readerId, bookId, chapterId)
        every { verifyAccessTokenService.execute(VerifyAccessTokenRequest("anytoken")) }
            .returns(VerifyAccessTokenResponse(anotherReaderId, "jkrowling", "jkrowling@gmail.com"))
        every { service.execute(serviceRequest) } returns BookChapterDoesNotBelongToReader

        RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .header("Authorization", "Bearer anytoken")
            .and()
            .get("/v1/readers/$readerId/books/$bookId/chapters/$chapterId")
            .then()
            .statusCode(404)
    }

    @Test
    fun `return ok with requested book chapter`() {
        val serviceRequest = GetReaderBookChapterRequest(readerId, bookId, chapterId)
        val serviceResponse = FullChapterResponseMother().anyChapter(bookId, chapterId)
        every { verifyAccessTokenService.execute(VerifyAccessTokenRequest("anytoken")) }
            .returns(VerifyAccessTokenResponse(anotherReaderId, "jkrowling", "jkrowling@gmail.com"))
        every { service.execute(serviceRequest) } returns serviceResponse

        RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .header("Authorization", "Bearer anytoken")
            .and()
            .get("/v1/readers/$readerId/books/$bookId/chapters/$chapterId")
            .then()
            .statusCode(200)
            .body("id", equalTo(serviceResponse.id))
            .body("title", equalTo(serviceResponse.title))
            .body("content", equalTo(serviceResponse.content))
            .body("price.amount", equalTo(serviceResponse.priceAmount))
            .body("price.currency", equalTo(serviceResponse.priceCurrency))
            .body("author", equalTo(serviceResponse.authorId))
            .body("book", equalTo(serviceResponse.bookId))
            .body("modifiedAt", equalTo(serviceResponse.modifiedAt.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)))
            .body("order", equalTo(serviceResponse.order))
            .body("excerpt", equalTo(serviceResponse.excerpt))
            .body("publishedAt", equalTo(serviceResponse.publishedAt.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)))
    }

    companion object {
        private val readerId = UUID.randomUUID().toString()
        private val anotherReaderId = UUID.randomUUID().toString()
        private val bookId = UUID.randomUUID().toString()
        private val chapterId = UUID.randomUUID().toString()
    }
}