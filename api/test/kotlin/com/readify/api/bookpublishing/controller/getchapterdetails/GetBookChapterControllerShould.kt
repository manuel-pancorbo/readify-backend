package com.readify.api.bookpublishing.controller.getchapterdetails

import com.ninjasquad.springmockk.MockkBean
import com.readify.ContractTest
import com.readify.authentication.application.service.verifyaccesstoken.VerifyAccessTokenRequest
import com.readify.authentication.application.service.verifyaccesstoken.VerifyAccessTokenResponse
import com.readify.authentication.application.service.verifyaccesstoken.VerifyAccessTokenService
import com.readify.bookpublishing.application.service.getchapterservice.ChapterNotFoundResponse
import com.readify.bookpublishing.application.service.getchapterservice.GetChapterRequest
import com.readify.bookpublishing.application.service.getchapterservice.GetChapterService
import io.mockk.every
import io.restassured.RestAssured
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Test
import java.time.format.DateTimeFormatter

class GetBookChapterControllerShould : ContractTest() {
    @MockkBean
    private lateinit var getChapterService: GetChapterService

    @MockkBean(relaxed = true)
    private lateinit var verifyAccessTokenService: VerifyAccessTokenService

    @Test
    fun `returns unauthorized when user is not authenticated`() {
        RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .get("/v1/books/$bookId/chapters/$chapterId")
            .then()
            .statusCode(401)
    }

    @Test
    fun `return 404 when the requested book does not exists`() {
        val serviceRequest = GetChapterRequest(authorId, bookId, chapterId)
        every { verifyAccessTokenService.execute(VerifyAccessTokenRequest("anytoken")) }
            .returns(VerifyAccessTokenResponse(authorId, "jkrowling", "jkrowling@gmail.com"))
        every { getChapterService.execute(serviceRequest) } returns ChapterNotFoundResponse

        RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .header("Authorization", "Bearer anytoken")
            .and()
            .get("/v1/books/$bookId/chapters/$chapterId")
            .then()
            .statusCode(404)
    }

    @Test
    fun `return 404 when the requested book chapter does not exists`() {
        val serviceRequest = GetChapterRequest(authorId, bookId, chapterId)
        every { verifyAccessTokenService.execute(VerifyAccessTokenRequest("anytoken")) }
            .returns(VerifyAccessTokenResponse(authorId, "jkrowling", "jkrowling@gmail.com"))
        every { getChapterService.execute(serviceRequest) } returns ChapterNotFoundResponse

        RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .header("Authorization", "Bearer anytoken")
            .and()
            .get("/v1/books/$bookId/chapters/$chapterId")
            .then()
            .statusCode(404)
    }

    @Test
    fun `return ok when chapter is created successfully`() {
        val serviceRequest = GetChapterRequest(authorId, bookId, chapterId)
        val serviceResponse = GetChapterResponseMother().anyChapter(authorId, bookId, chapterId)
        every { verifyAccessTokenService.execute(VerifyAccessTokenRequest("anytoken")) }
            .returns(VerifyAccessTokenResponse(authorId, "jkrowling", "jkrowling@gmail.com"))
        every { getChapterService.execute(serviceRequest) } returns serviceResponse

        RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .header("Authorization", "Bearer anytoken")
            .and()
            .get("/v1/books/$bookId/chapters/$chapterId")
            .then()
            .statusCode(200)
            .body("id", equalTo(serviceResponse.id))
            .body("title", equalTo(serviceResponse.title))
            .body("content", equalTo(serviceResponse.content))
            .body("status", equalTo(serviceResponse.status.toString().toLowerCase()))
            .body("author", equalTo(serviceResponse.authorId))
            .body("book", equalTo(serviceResponse.bookId))
            .body("price.amount", equalTo(serviceResponse.priceAmount))
            .body("price.currency", equalTo(serviceResponse.priceCurrency))
            .body("order", equalTo(serviceResponse.order))
            .body("excerpt", equalTo(serviceResponse.excerpt))
            .body(
                "modifiedAt",
                equalTo(serviceResponse.modifiedAt.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
            )
    }

    companion object {
        private const val bookId = "any-book-id"
        private const val chapterId = "any-chapter-id"
        private const val authorId = "any-author-id"
    }
}