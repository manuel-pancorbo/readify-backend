package com.readify.api.bookpublishing.controller.createbook

import com.ninjasquad.springmockk.MockkBean
import com.readify.ContractTest
import com.readify.authentication.application.service.verifyaccesstoken.VerifyAccessTokenRequest
import com.readify.authentication.application.service.verifyaccesstoken.VerifyAccessTokenResponse
import com.readify.authentication.application.service.verifyaccesstoken.VerifyAccessTokenService
import com.readify.bookpublishing.application.service.createbook.PublishBookRequest
import com.readify.bookpublishing.application.service.createbook.PublishBookResponse
import com.readify.bookpublishing.application.service.createbook.PublishBookService
import io.mockk.every
import io.restassured.RestAssured
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Test

class PostBookControllerShould : ContractTest() {
    @MockkBean(relaxed = true)
    private lateinit var publishBookService: PublishBookService

    @MockkBean(relaxed = true)
    private lateinit var verifyAccessTokenService: VerifyAccessTokenService

    @Test
    fun `returns unauthorized when user is not authenticated`() {
        RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .body(bookBody())
            .post("/v1/books")
            .then()
            .statusCode(401)
    }

    @Test
    fun `returns ok when book has been successfully created`() {
        every { verifyAccessTokenService.execute(VerifyAccessTokenRequest("anytoken")) }
            .returns(VerifyAccessTokenResponse("any-author-id", "jkrowling", "jkrowling@gmail.com"))
        every {
            publishBookService.execute(
                PublishBookRequest(
                    "any-author-id",
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
                PublishBookResponse(
                    "any-author-id", "any-id",
                    TITLE,
                    SUMMARY,
                    COVER,
                    tags,
                    PRICE,
                    CURRENCY
                )
            )

        RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .header("Authorization", "Bearer anytoken")
            .body(bookBody())
            .post("/v1/books")
            .then()
            .statusCode(200)
            .body("id", equalTo("any-id"))
            .body("title", equalTo(TITLE))
            .body("summary", equalTo(SUMMARY))
            .body("cover", equalTo(COVER))
            .body("tags", CoreMatchers.hasItems(tags[0], tags[1]))
            .body("price.amount", equalTo(PRICE))
            .body("price.currency", equalTo(CURRENCY))
    }

    private fun bookBody() =
        """{
                    "title": "$TITLE",
                    "summary": "$SUMMARY",
                    "cover": "$COVER",
                    "tags": ["${tags[0]}", "${tags[1]}"],
                    "price": {
                        "amount": 15,
                        "currency": "EUR"
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
    }
}