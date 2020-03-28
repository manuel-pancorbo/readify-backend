package com.readify.api.bookpublishing.controller.updatebook

import com.ninjasquad.springmockk.MockkBean
import com.readify.ContractTest
import com.readify.authentication.application.service.verifyaccesstoken.VerifyAccessTokenRequest
import com.readify.authentication.application.service.verifyaccesstoken.VerifyAccessTokenResponse
import com.readify.authentication.application.service.verifyaccesstoken.VerifyAccessTokenService
import com.readify.bookpublishing.application.service.common.BookStatus
import com.readify.bookpublishing.application.service.common.BookVisibility
import com.readify.bookpublishing.application.service.updatebook.BookNotBelongToAuthorResponse
import com.readify.bookpublishing.application.service.updatebook.BookNotFoundResponse
import com.readify.bookpublishing.application.service.updatebook.BookUpdatedSuccessfully
import com.readify.bookpublishing.application.service.updatebook.CompletionPercentageOutOfRange
import com.readify.bookpublishing.application.service.updatebook.UpdateBookRequest
import com.readify.bookpublishing.application.service.updatebook.UpdateBookService
import io.mockk.every
import io.restassured.RestAssured
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.nullValue
import org.junit.jupiter.api.Test

class PatchBookControllerShould : ContractTest() {
    @MockkBean
    private lateinit var updateBookService: UpdateBookService

    @MockkBean(relaxed = true)
    private lateinit var verifyAccessTokenService: VerifyAccessTokenService

    @Test
    fun `returns unauthorized when user is not authenticated`() {
        RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .body(updateBookBody())
            .patch("/v1/books/$ANY_BOOK_ID")
            .then()
            .statusCode(401)
    }

    @Test
    fun `returns 200 when book has been updated successfully`() {
        val serviceRequest = UpdateBookRequest(ANY_AUTHOR_ID, ANY_BOOK_ID, 50)
        every { verifyAccessTokenService.execute(VerifyAccessTokenRequest("anytoken")) }
            .returns(VerifyAccessTokenResponse(ANY_AUTHOR_ID, "jkrowling", "jkrowling@gmail.com"))
        every { updateBookService.execute(serviceRequest) } returns bookUpdatedResponse()

        RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .header("Authorization", "Bearer anytoken")
            .and()
            .body(updateBookBody())
            .patch("/v1/books/$ANY_BOOK_ID")
            .then()
            .statusCode(200)
            .body("id", equalTo(ANY_BOOK_ID))
            .body("status", equalTo("in_progress"))
            .body("visibility", equalTo("null"))
            .body("title", equalTo(ANY_TITLE))
            .body("summary", equalTo(ANY_SUMMARY))
            .body("cover", equalTo(ANY_COVER))
            .body("tags", equalTo(ANY_TAGS))
            .body("price.amount", equalTo(ANY_AMOUNT))
            .body("price.currency", equalTo(ANY_CURRENCY))
            .body("completionPercentage", equalTo(ANY_COMPLETION_PERCENTAGE))
            .body("finishedAt", nullValue())
    }

    @Test
    fun `returns 400 when new percentage is out of valid range`() {
        val serviceRequest = UpdateBookRequest(ANY_AUTHOR_ID, ANY_BOOK_ID, 50)
        every { verifyAccessTokenService.execute(VerifyAccessTokenRequest("anytoken")) }
            .returns(VerifyAccessTokenResponse(ANY_AUTHOR_ID, "jkrowling", "jkrowling@gmail.com"))
        every { updateBookService.execute(serviceRequest) } returns (CompletionPercentageOutOfRange)

        RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .header("Authorization", "Bearer anytoken")
            .and()
            .body(updateBookBody())
            .patch("/v1/books/$ANY_BOOK_ID")
            .then()
            .statusCode(400)
            .body("code", equalTo("bookpublishing.percentage_completion"))
            .body("message", equalTo("Completion percentage out of range"))
            .body("field", equalTo("completionPercentage"))
    }

    @Test
    fun `returns 404 when an user wants to update a book of another user`() {
        val serviceRequest = UpdateBookRequest(ANY_AUTHOR_ID, ANY_BOOK_ID, 50)
        every { verifyAccessTokenService.execute(VerifyAccessTokenRequest("anytoken")) }
            .returns(VerifyAccessTokenResponse(ANY_AUTHOR_ID, "jkrowling", "jkrowling@gmail.com"))
        every { updateBookService.execute(serviceRequest) } returns (BookNotBelongToAuthorResponse)

        RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .header("Authorization", "Bearer anytoken")
            .and()
            .body(updateBookBody())
            .patch("/v1/books/$ANY_BOOK_ID")
            .then()
            .statusCode(404)
    }

    @Test
    fun `returns 404 when an user wants to update a book that not exists`() {
        val serviceRequest = UpdateBookRequest(ANY_AUTHOR_ID, ANY_BOOK_ID, 50)
        every { verifyAccessTokenService.execute(VerifyAccessTokenRequest("anytoken")) }
            .returns(VerifyAccessTokenResponse(ANY_AUTHOR_ID, "jkrowling", "jkrowling@gmail.com"))
        every { updateBookService.execute(serviceRequest) } returns (BookNotFoundResponse)

        RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .header("Authorization", "Bearer anytoken")
            .and()
            .body(updateBookBody())
            .patch("/v1/books/$ANY_BOOK_ID")
            .then()
            .statusCode(404)
    }

    private fun bookUpdatedResponse() = BookUpdatedSuccessfully(
        ANY_AUTHOR_ID, ANY_BOOK_ID, ANY_TITLE, ANY_SUMMARY, ANY_COVER, ANY_TAGS, ANY_AMOUNT, ANY_CURRENCY, ANY_STATUS,
        ANY_VISIBILITY, null, ANY_COMPLETION_PERCENTAGE
    )

    private fun updateBookBody() = """
        {
          "completionPercentage": 50
        }
    """

    companion object {
        private const val ANY_AUTHOR_ID = "any-author-id"
        private const val ANY_BOOK_ID = "8a956e9c-5a63-42f9-9afc-7a406ff48285"
        private const val ANY_TITLE = "Any title"
        private const val ANY_SUMMARY = "Any summary"
        private const val ANY_COVER = "http://any-cover.com/cover.jpg"
        private val ANY_TAGS = listOf("any", "tag")
        private const val ANY_AMOUNT = 15f
        private const val ANY_CURRENCY = "EUR"
        private val ANY_STATUS = BookStatus.IN_PROGRESS
        private val ANY_VISIBILITY = BookVisibility.NULL
        private const val ANY_COMPLETION_PERCENTAGE = 50
    }
}
