package com.readify.api.bookpublishing.controller.createchapter

import com.ninjasquad.springmockk.MockkBean
import com.readify.ContractTest
import com.readify.authentication.application.service.verifyaccesstoken.VerifyAccessTokenRequest
import com.readify.authentication.application.service.verifyaccesstoken.VerifyAccessTokenResponse
import com.readify.authentication.application.service.verifyaccesstoken.VerifyAccessTokenService
import com.readify.bookpublishing.application.service.createchapter.BookNotBelongToAuthorResponse
import com.readify.bookpublishing.application.service.createchapter.BookNotFoundResponse
import com.readify.bookpublishing.application.service.createchapter.CreateChapterService
import io.mockk.every
import io.mockk.verify
import io.restassured.RestAssured
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Test
import java.time.format.DateTimeFormatter

class PostBookChapterControllerShould : ContractTest() {
    @MockkBean
    private lateinit var createChapterService: CreateChapterService

    @MockkBean(relaxed = true)
    private lateinit var verifyAccessTokenService: VerifyAccessTokenService

    @Test
    fun `returns unauthorized when user is not authenticated`() {
        RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .body(chapterBody(ChapterHttpRequestMother().validOne()))
            .post("/v1/books/$bookId/chapters")
            .then()
            .statusCode(401)
    }

    @Test
    fun `return 404 when the requested book does not exists`() {
        val validRequest = ChapterHttpRequestMother().validOne()
        val serviceRequest =
            CreateChapterRequestMother().createWith("any-author-id", bookId)
        every { verifyAccessTokenService.execute(VerifyAccessTokenRequest("anytoken")) }
            .returns(VerifyAccessTokenResponse("any-author-id", "jkrowling", "jkrowling@gmail.com"))
        every { createChapterService.execute(any()) } returns BookNotFoundResponse

        RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .header("Authorization", "Bearer anytoken")
            .and()
            .body(chapterBody(validRequest))
            .post("/v1/books/$bookId/chapters")
            .then()
            .statusCode(404)

        verify { createChapterService.execute(serviceRequest) }
    }

    @Test
    fun `return 404 when the requested book does not belong to requester author`() {
        val validRequest = ChapterHttpRequestMother().validOne()
        val serviceRequest =
            CreateChapterRequestMother().createWith("any-author-id", bookId)
        every { verifyAccessTokenService.execute(VerifyAccessTokenRequest("anytoken")) }
            .returns(VerifyAccessTokenResponse("any-author-id", "jkrowling", "jkrowling@gmail.com"))
        every { createChapterService.execute(any()) } returns BookNotBelongToAuthorResponse

        RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .header("Authorization", "Bearer anytoken")
            .and()
            .body(chapterBody(validRequest))
            .post("/v1/books/$bookId/chapters")
            .then()
            .statusCode(404)

        verify { createChapterService.execute(serviceRequest) }
    }

    @Test
    fun `return ok when chapter is created successfully`() {
        val validRequest = ChapterHttpRequestMother().validOne()
        val serviceRequest = CreateChapterRequestMother().createWith("any-author-id", bookId)
        val serviceResponse = ChapterCreatedResponseMother().createWith("any-author-id", bookId)
        every { verifyAccessTokenService.execute(VerifyAccessTokenRequest("anytoken")) }
            .returns(VerifyAccessTokenResponse("any-author-id", "jkrowling", "jkrowling@gmail.com"))
        every { createChapterService.execute(any()) }
            .returns(serviceResponse)

        RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .header("Authorization", "Bearer anytoken")
            .and()
            .body(chapterBody(validRequest))
            .post("/v1/books/$bookId/chapters")
            .then()
            .statusCode(200)
            .body("id", equalTo(serviceResponse.id))
            .body("title", equalTo(serviceResponse.title))
            .body("content", equalTo(serviceResponse.content))
            .body("status", equalTo(serviceResponse.status.toString()))
            .body("modifiedAt", equalTo(serviceResponse.modifiedAt.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)))
            .body("author", equalTo(serviceResponse.authorId))
            .body("book", equalTo(serviceResponse.bookId))

        verify { createChapterService.execute(serviceRequest) }
    }

    private fun chapterBody(request: PostBookChapterHttpRequest) =
        """
        {
            "title": "${request.title}",
            "content": "${request.content}"
        }
    """

    companion object {
        private const val bookId = "any-book-id"
    }
}