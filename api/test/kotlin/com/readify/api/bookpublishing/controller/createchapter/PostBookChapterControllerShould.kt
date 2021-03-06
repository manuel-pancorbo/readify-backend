package com.readify.api.bookpublishing.controller.createchapter

import com.ninjasquad.springmockk.MockkBean
import com.readify.ContractTest
import com.readify.authentication.application.service.verifyaccesstoken.VerifyAccessTokenRequest
import com.readify.authentication.application.service.verifyaccesstoken.VerifyAccessTokenResponse
import com.readify.authentication.application.service.verifyaccesstoken.VerifyAccessTokenService
import com.readify.bookpublishing.application.service.createchapter.BookNotBelongToAuthorResponse
import com.readify.bookpublishing.application.service.createchapter.BookNotFoundResponse
import com.readify.bookpublishing.application.service.createchapter.CreateChapterService
import com.readify.bookpublishing.application.service.createchapter.InvalidCurrencyResponse
import io.mockk.every
import io.mockk.verify
import io.restassured.RestAssured
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Test
import java.time.format.DateTimeFormatter
import java.util.UUID

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
            .post("/v1/authors/$authorId/books/$bookId/chapters")
            .then()
            .statusCode(401)
    }

    @Test
    fun `returns not found when requester is not the author of the resource`() {
        every { verifyAccessTokenService.execute(VerifyAccessTokenRequest("anytoken")) }
            .returns(VerifyAccessTokenResponse(anotherAuthorId, "jkrowling", "jkrowling@gmail.com"))

        RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .body(chapterBody(ChapterHttpRequestMother().validOne()))
            .post("/v1/authors/$authorId/books/$bookId/chapters")
            .then()
            .statusCode(401)
    }

    @Test
    fun `return 404 when the requested book does not exists`() {
        val validRequest = ChapterHttpRequestMother().validOne()
        val serviceRequest =
            CreateChapterRequestMother().validOne(authorId, bookId)
        every { verifyAccessTokenService.execute(VerifyAccessTokenRequest("anytoken")) }
            .returns(VerifyAccessTokenResponse(authorId, "jkrowling", "jkrowling@gmail.com"))
        every { createChapterService.execute(any()) } returns BookNotFoundResponse

        RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .header("Authorization", "Bearer anytoken")
            .and()
            .body(chapterBody(validRequest))
            .post("/v1/authors/$authorId/books/$bookId/chapters")
            .then()
            .statusCode(404)

        verify { createChapterService.execute(serviceRequest) }
    }

    @Test
    fun `return 404 when the requested book does not belong to requester author`() {
        val validRequest = ChapterHttpRequestMother().validOne()
        val serviceRequest =
            CreateChapterRequestMother().validOne(authorId, bookId)
        every { verifyAccessTokenService.execute(VerifyAccessTokenRequest("anytoken")) }
            .returns(VerifyAccessTokenResponse(authorId, "jkrowling", "jkrowling@gmail.com"))
        every { createChapterService.execute(any()) } returns BookNotBelongToAuthorResponse

        RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .header("Authorization", "Bearer anytoken")
            .and()
            .body(chapterBody(validRequest))
            .post("/v1/authors/$authorId/books/$bookId/chapters")
            .then()
            .statusCode(404)

        verify { createChapterService.execute(serviceRequest) }
    }

    @Test
    fun `return ok when chapter is created successfully`() {
        val validRequest = ChapterHttpRequestMother().validOne()
        val serviceRequest = CreateChapterRequestMother().validOne(authorId, bookId)
        val serviceResponse = ChapterCreatedResponseMother().createWith(authorId, bookId)
        every { verifyAccessTokenService.execute(VerifyAccessTokenRequest("anytoken")) }
            .returns(VerifyAccessTokenResponse(authorId, "jkrowling", "jkrowling@gmail.com"))
        every { createChapterService.execute(any()) }
            .returns(serviceResponse)

        RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .header("Authorization", "Bearer anytoken")
            .and()
            .body(chapterBody(validRequest))
            .post("/v1/authors/$authorId/books/$bookId/chapters")
            .then()
            .statusCode(200)
            .body("id", equalTo(serviceResponse.id))
            .body("title", equalTo(serviceResponse.title))
            .body("content", equalTo(serviceResponse.content))
            .body("status", equalTo(serviceResponse.status.toString().toLowerCase()))
            .body("modifiedAt", equalTo(serviceResponse.modifiedAt.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)))
            .body("author", equalTo(serviceResponse.authorId))
            .body("book", equalTo(serviceResponse.bookId))
            .body("price.amount", equalTo(serviceResponse.priceAmount))
            .body("price.currency", equalTo(serviceResponse.priceCurrency))
            .body("order", equalTo(serviceResponse.order))
            .body("excerpt", equalTo(serviceResponse.excerpt))

        verify { createChapterService.execute(serviceRequest) }
    }

    @Test
    fun `return bad request when given currency is not supported`() {
        val validRequest = ChapterHttpRequestMother().unsupportedCurrency()
        val serviceRequest = CreateChapterRequestMother().unsupportedCurrency(authorId, bookId)
        every { verifyAccessTokenService.execute(VerifyAccessTokenRequest("anytoken")) }
            .returns(VerifyAccessTokenResponse(authorId, "jkrowling", "jkrowling@gmail.com"))
        every { createChapterService.execute(serviceRequest) }
            .returns(InvalidCurrencyResponse)

        RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .header("Authorization", "Bearer anytoken")
            .and()
            .body(chapterBody(validRequest))
            .post("/v1/authors/$authorId/books/$bookId/chapters")
            .then()
            .statusCode(400)
            .body("code", equalTo("bookpublishing.currency_not_supported"))
            .body("message", equalTo("Currency not supported"))
            .body("field", equalTo("price"))

        verify { createChapterService.execute(serviceRequest) }
    }

    @Test
    fun `return bad request when not all mandatory parameters are supplied`() {
        RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .header("Authorization", "Bearer anytoken")
            .and()
            .body(invalidChapterBody())
            .post("/v1/authors/$authorId/books/$bookId/chapters")
            .then()
            .statusCode(400)
    }

    private fun chapterBody(request: PostBookChapterHttpRequest) =
        """
        {
            "title": "${request.title}",
            "content": "${request.content}",
            "price": {
                "amount": ${request.price.amount},
                "currency": "${request.price.currency}"
            },
            "order": ${request.order},
            "excerpt": "${request.excerpt}"
        }
    """

    private fun invalidChapterBody() =
        """
        {
            "title": "any title",
            "content": "any content",
            "price": {
                "amount": 40,
                "currency": "EUR"
            }
        }
        """

    companion object {
        private val bookId = UUID.randomUUID().toString()
        private val authorId = UUID.randomUUID().toString()
        private val anotherAuthorId = UUID.randomUUID().toString()
    }
}