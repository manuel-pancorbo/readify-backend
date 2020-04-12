package com.readify.api.bookpublishing.controller.getbookdetails

import com.ninjasquad.springmockk.MockkBean
import com.readify.ContractTest
import com.readify.authentication.application.service.verifyaccesstoken.VerifyAccessTokenRequest
import com.readify.authentication.application.service.verifyaccesstoken.VerifyAccessTokenResponse
import com.readify.authentication.application.service.verifyaccesstoken.VerifyAccessTokenService
import com.readify.bookpublishing.application.service.getbook.BookNotFoundResponse
import com.readify.bookpublishing.application.service.getbook.GetBookRequest
import com.readify.bookpublishing.application.service.getbook.GetBookService
import io.mockk.every
import io.restassured.RestAssured
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Test
import java.time.format.DateTimeFormatter
import java.util.UUID

class GetBookControllerShould : ContractTest() {
    @MockkBean
    private lateinit var getBookService: GetBookService

    @MockkBean(relaxed = true)
    private lateinit var verifyAccessTokenService: VerifyAccessTokenService

    @Test
    fun `returns unauthorized when user is not authenticated`() {
        RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .get("/v1/authors/$authorId/books/$bookId")
            .then()
            .statusCode(401)
    }

    @Test
    fun `return 404 when the requested book does not exists`() {
        val serviceRequest = GetBookRequest(authorId, bookId)
        every { verifyAccessTokenService.execute(VerifyAccessTokenRequest("anytoken")) }
            .returns(VerifyAccessTokenResponse(authorId, "jkrowling", "jkrowling@gmail.com"))
        every { getBookService.execute(serviceRequest) } returns BookNotFoundResponse

        RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .header("Authorization", "Bearer anytoken")
            .and()
            .get("/v1/authors/$authorId/books/$bookId")
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
            .get("/v1/authors/$authorId/books/$bookId")
            .then()
            .statusCode(404)
    }

    @Test
    fun `return ok when chapter is created successfully`() {
        val serviceResponse = GetBookResponseMother().anyBook(authorId, bookId)
        val serviceRequest = GetBookRequest(authorId, bookId)
        every { verifyAccessTokenService.execute(VerifyAccessTokenRequest("anytoken")) }
            .returns(VerifyAccessTokenResponse(authorId, "jkrowling", "jkrowling@gmail.com"))
        every { getBookService.execute(serviceRequest) } returns serviceResponse

        RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .header("Authorization", "Bearer anytoken")
            .and()
            .get("/v1/authors/$authorId/books/$bookId")
            .then()
            .statusCode(200)
            .body("id", equalTo(bookId))
            .body("status", equalTo(serviceResponse.status.toString().toLowerCase()))
            .body("visibility", equalTo(serviceResponse.visibility.toString().toLowerCase()))
            .body("title", equalTo(serviceResponse.title))
            .body("summary", equalTo(serviceResponse.summary))
            .body("cover", equalTo(serviceResponse.cover))
            .body("tags", equalTo(serviceResponse.tags))
            .body("price.amount", equalTo(serviceResponse.priceAmount))
            .body("price.currency", equalTo(serviceResponse.priceCurrency))
            .body("completionPercentage", equalTo(serviceResponse.completionPercentage))
            .body("finishedAt", equalTo(serviceResponse.finishedAt?.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)))
    }

    companion object {
        private val bookId = UUID.randomUUID().toString()
        private val authorId = UUID.randomUUID().toString()
        private val anotherAuthorId = UUID.randomUUID().toString()
    }
}