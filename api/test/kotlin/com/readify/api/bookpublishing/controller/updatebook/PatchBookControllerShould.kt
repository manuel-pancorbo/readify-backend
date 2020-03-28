package com.readify.api.bookpublishing.controller.updatebook

import com.ninjasquad.springmockk.MockkBean
import com.readify.ContractTest
import com.readify.authentication.application.service.verifyaccesstoken.VerifyAccessTokenRequest
import com.readify.authentication.application.service.verifyaccesstoken.VerifyAccessTokenResponse
import com.readify.authentication.application.service.verifyaccesstoken.VerifyAccessTokenService
import com.readify.bookpublishing.application.service.updatebook.BookNotBelongToAuthorResponse
import com.readify.bookpublishing.application.service.updatebook.BookNotFoundResponse
import com.readify.bookpublishing.application.service.updatebook.BookUpdatedSuccessfully
import com.readify.bookpublishing.application.service.updatebook.CompletionPercentageOutOfRange
import com.readify.bookpublishing.application.service.updatebook.UpdateBookRequest
import com.readify.bookpublishing.application.service.updatebook.UpdateBookService
import io.mockk.every
import io.restassured.RestAssured
import org.hamcrest.CoreMatchers
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
            .patch("/v1/books/8a956e9c-5a63-42f9-9afc-7a406ff48285")
            .then()
            .statusCode(401)
    }

    @Test
    fun `returns 200 when book has been updated successfully`() {
        val serviceRequest = UpdateBookRequest("any-author-id", "8a956e9c-5a63-42f9-9afc-7a406ff48285", 50)
        every { verifyAccessTokenService.execute(VerifyAccessTokenRequest("anytoken")) }
            .returns(VerifyAccessTokenResponse("any-author-id", "jkrowling", "jkrowling@gmail.com"))
        every { updateBookService.execute(serviceRequest) } returns BookUpdatedSuccessfully

        RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .header("Authorization", "Bearer anytoken")
            .and()
            .body(updateBookBody())
            .patch("/v1/books/8a956e9c-5a63-42f9-9afc-7a406ff48285")
            .then()
            .statusCode(200)
    }

    @Test
    fun `returns 400 when new percentage is out of valid range`() {
        val serviceRequest = UpdateBookRequest("any-author-id", "8a956e9c-5a63-42f9-9afc-7a406ff48285", 50)
        every { verifyAccessTokenService.execute(VerifyAccessTokenRequest("anytoken")) }
            .returns(VerifyAccessTokenResponse("any-author-id", "jkrowling", "jkrowling@gmail.com"))
        every { updateBookService.execute(serviceRequest) } returns(CompletionPercentageOutOfRange)

        RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .header("Authorization", "Bearer anytoken")
            .and()
            .body(updateBookBody())
            .patch("/v1/books/8a956e9c-5a63-42f9-9afc-7a406ff48285")
            .then()
            .statusCode(400)
            .body("code", CoreMatchers.equalTo("bookpublishing.percentage_completion"))
            .body("message", CoreMatchers.equalTo("Completion percentage out of range"))
            .body("field", CoreMatchers.equalTo("completionPercentage"))
    }

    @Test
    fun `returns 404 when an user wants to update a book of another user`() {
        val serviceRequest = UpdateBookRequest("any-author-id", "8a956e9c-5a63-42f9-9afc-7a406ff48285", 50)
        every { verifyAccessTokenService.execute(VerifyAccessTokenRequest("anytoken")) }
            .returns(VerifyAccessTokenResponse("any-author-id", "jkrowling", "jkrowling@gmail.com"))
        every { updateBookService.execute(serviceRequest) } returns(BookNotBelongToAuthorResponse)

        RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .header("Authorization", "Bearer anytoken")
            .and()
            .body(updateBookBody())
            .patch("/v1/books/8a956e9c-5a63-42f9-9afc-7a406ff48285")
            .then()
            .statusCode(404)
    }

    @Test
    fun `returns 404 when an user wants to update a book that not exists`() {
        val serviceRequest = UpdateBookRequest("any-author-id", "8a956e9c-5a63-42f9-9afc-7a406ff48285", 50)
        every { verifyAccessTokenService.execute(VerifyAccessTokenRequest("anytoken")) }
            .returns(VerifyAccessTokenResponse("any-author-id", "jkrowling", "jkrowling@gmail.com"))
        every { updateBookService.execute(serviceRequest) } returns(BookNotFoundResponse)

        RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .header("Authorization", "Bearer anytoken")
            .and()
            .body(updateBookBody())
            .patch("/v1/books/8a956e9c-5a63-42f9-9afc-7a406ff48285")
            .then()
            .statusCode(404)
    }

    private fun updateBookBody() = """
        {
          "completionPercentage": 50
        }
    """
}
