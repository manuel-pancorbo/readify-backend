package com.readify.api.bookpublishing.controller.createbook

import com.ninjasquad.springmockk.MockkBean
import com.readify.ContractTest
import com.readify.authentication.application.service.verifyaccesstoken.VerifyAccessTokenRequest
import com.readify.authentication.application.service.verifyaccesstoken.VerifyAccessTokenResponse
import com.readify.authentication.application.service.verifyaccesstoken.VerifyAccessTokenService
import com.readify.bookpublishing.application.service.common.BookStatus
import com.readify.bookpublishing.application.service.common.BookVisibility
import com.readify.bookpublishing.application.service.createbook.BookCreatedSuccessfullyResponse
import com.readify.bookpublishing.application.service.createbook.CreateBookRequest
import com.readify.bookpublishing.application.service.createbook.CreateBookService
import com.readify.bookpublishing.application.service.createbook.InvalidCurrencyResponse
import io.mockk.every
import io.restassured.RestAssured
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.nullValue
import org.junit.jupiter.api.Test
import java.util.UUID

class PostBookControllerShould : ContractTest() {
    @MockkBean(relaxed = true)
    private lateinit var createBookService: CreateBookService

    @MockkBean(relaxed = true)
    private lateinit var verifyAccessTokenService: VerifyAccessTokenService

    @Test
    fun `returns unauthorized when user is not authenticated`() {
        RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .body(bookBody())
            .post("/v1/authors/$authorId/books")
            .then()
            .statusCode(401)
    }

    @Test
    fun `returns 404 when requester is not the resource author`() {
        every { verifyAccessTokenService.execute(VerifyAccessTokenRequest("anytoken")) }
            .returns(VerifyAccessTokenResponse(anotherAuthorId, "jkrowling", "jkrowling@gmail.com"))

        RestAssured.given()
            .`when`()
            .contentType("application/json")
            .header("Authorization", "Bearer anytoken")
            .and()
            .body(bookBody())
            .post("/v1/authors/$authorId/books")
            .then()
            .statusCode(404)
    }

    @Test
    fun `returns bad request when currency is not supported`() {
        every { verifyAccessTokenService.execute(VerifyAccessTokenRequest("anytoken")) }
            .returns(VerifyAccessTokenResponse(authorId, "jkrowling", "jkrowling@gmail.com"))

        every {
            createBookService.execute(
                CreateBookRequest(
                    authorId,
                    TITLE,
                    SUMMARY,
                    COVER,
                    tags,
                    PRICE,
                    "not-supported-currency"
                )
            )
        }
            .returns(InvalidCurrencyResponse)

        RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .header("Authorization", "Bearer anytoken")
            .body(bookBody(currency = "not-supported-currency"))
            .post("/v1/authors/$authorId/books")
            .then()
            .statusCode(400)
            .body("code", equalTo("bookpublishing.currency_not_supported"))
            .body("message", equalTo("Currency not supported"))
            .body("field", equalTo("price"))
    }

    @Test
    fun `returns ok when book has been successfully created`() {
        every { verifyAccessTokenService.execute(VerifyAccessTokenRequest("anytoken")) }
            .returns(VerifyAccessTokenResponse(authorId, "jkrowling", "jkrowling@gmail.com"))
        every {
            createBookService.execute(
                CreateBookRequest(
                    authorId,
                    TITLE,
                    SUMMARY,
                    COVER,
                    tags,
                    PRICE,
                    CURRENCY
                )
            )
        }
            .returns(
                BookCreatedSuccessfullyResponse(
                    authorId, "any-id",
                    TITLE,
                    SUMMARY,
                    COVER,
                    tags,
                    PRICE,
                    CURRENCY,
                    BookStatus.IN_PROGRESS,
                    BookVisibility.NULL,
                    null,
                    0
                )
            )

        RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .header("Authorization", "Bearer anytoken")
            .body(bookBody())
            .post("/v1/authors/$authorId/books")
            .then()
            .statusCode(200)
            .body("id", equalTo("any-id"))
            .body("status", equalTo("in_progress"))
            .body("visibility", equalTo("null"))
            .body("title", equalTo(TITLE))
            .body("summary", equalTo(SUMMARY))
            .body("cover", equalTo(COVER))
            .body("tags", CoreMatchers.hasItems(tags[0], tags[1]))
            .body("price.amount", equalTo(PRICE))
            .body("price.currency", equalTo(CURRENCY))
            .body("completionPercentage", equalTo(0))
            .body("finishedAt", nullValue())
    }

    private fun bookBody(currency: String = CURRENCY) =
        """{
                    "title": "$TITLE",
                    "summary": "$SUMMARY",
                    "cover": "$COVER",
                    "tags": ["${tags[0]}", "${tags[1]}"],
                    "price": {
                        "amount": 15,
                        "currency": "$currency"
                    }
                }"""

    companion object {
        const val TITLE = "Harry Potter and the philosopher's stone"
        const val SUMMARY =
            "Harry hasn't had a birthday party in eleven years - but all that is about to change when a mysterious letter arrives with an invitation to an incredible place."
        const val COVER = "https://images-na.ssl-images-amazon.com/images/I/51HSkTKlauL._SX346_BO1,204,203,200_.jpg"
        const val PRICE = 15f
        const val CURRENCY = "EUR"
        val tags = listOf("fantasy", "magic")
        val authorId = UUID.randomUUID().toString()
        val anotherAuthorId = UUID.randomUUID().toString()
    }
}