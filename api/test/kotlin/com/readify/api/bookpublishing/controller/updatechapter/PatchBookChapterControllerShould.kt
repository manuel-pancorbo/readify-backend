package com.readify.api.bookpublishing.controller.updatechapter

import com.ninjasquad.springmockk.MockkBean
import com.readify.ContractTest
import com.readify.authentication.application.service.verifyaccesstoken.VerifyAccessTokenRequest
import com.readify.authentication.application.service.verifyaccesstoken.VerifyAccessTokenResponse
import com.readify.authentication.application.service.verifyaccesstoken.VerifyAccessTokenService
import com.readify.bookpublishing.application.service.updatechapter.BookChapterNotFoundResponse
import com.readify.bookpublishing.application.service.updatechapter.BookNotBelongToAuthorResponse
import com.readify.bookpublishing.application.service.updatechapter.InvalidChapterStatusResponse
import com.readify.bookpublishing.application.service.updatechapter.UpdateBookChapterRequest
import com.readify.bookpublishing.application.service.updatechapter.UpdateBookChapterService
import io.mockk.every
import io.restassured.RestAssured
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Test
import java.time.format.DateTimeFormatter

class PatchBookChapterControllerShould : ContractTest() {
    @MockkBean
    private lateinit var updateBookChapterService: UpdateBookChapterService

    @MockkBean(relaxed = true)
    private lateinit var verifyAccessTokenService: VerifyAccessTokenService

    @Test
    fun `returns unauthorized when user is not authenticated`() {
        RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .body(updateBookChapterBody())
            .patch("/v1/books/$ANY_BOOK_ID/chapters/$ANY_CHAPTER_ID")
            .then()
            .statusCode(401)
    }

    @Test
    fun `returns 404 when an user wants to update a chapter that not exists`() {
        every { verifyAccessTokenService.execute(VerifyAccessTokenRequest("anytoken")) }
            .returns(VerifyAccessTokenResponse(ANY_AUTHOR_ID, "jkrowling", "jkrowling@gmail.com"))
        every { updateBookChapterService.execute(serviceRequest()) } returns BookChapterNotFoundResponse

        RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .header("Authorization", "Bearer anytoken")
            .and()
            .body(updateBookChapterBody())
            .patch("/v1/books/${ANY_BOOK_ID}/chapters/$ANY_CHAPTER_ID")
            .then()
            .statusCode(404)
    }

    @Test
    fun `returns 404 when an author wants to update a book chapter of another author`() {
        every { verifyAccessTokenService.execute(VerifyAccessTokenRequest("anytoken")) }
            .returns(VerifyAccessTokenResponse(ANY_AUTHOR_ID, "jkrowling", "jkrowling@gmail.com"))
        every { updateBookChapterService.execute(serviceRequest()) } returns BookNotBelongToAuthorResponse

        RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .header("Authorization", "Bearer anytoken")
            .and()
            .body(updateBookChapterBody())
            .patch("/v1/books/${ANY_BOOK_ID}/chapters/$ANY_CHAPTER_ID")
            .then()
            .statusCode(404)
    }

    @Test
    fun `returns 400 when an author wants to set a book chapter as draft before setting it as published`() {
        every { verifyAccessTokenService.execute(VerifyAccessTokenRequest("anytoken")) }
            .returns(VerifyAccessTokenResponse(ANY_AUTHOR_ID, "jkrowling", "jkrowling@gmail.com"))
        every { updateBookChapterService.execute(serviceRequest(status = "draft")) }
            .returns(InvalidChapterStatusResponse)

        RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .header("Authorization", "Bearer anytoken")
            .and()
            .body(updateBookChapterBody(status = "draft"))
            .patch("/v1/books/${ANY_BOOK_ID}/chapters/$ANY_CHAPTER_ID")
            .then()
            .statusCode(400)
            .body("code", equalTo("bookpublishing.chapter.invalid-status"))
            .body("message", equalTo("Invalid chapter status"))
            .body("field", equalTo("status"))
    }

    @Test
    fun `returns ok when an author update a chapter to set it as published`() {
        val serviceResponse = ChapterUpdatedResponseMother().createWith(ANY_AUTHOR_ID, ANY_BOOK_ID)
        every { verifyAccessTokenService.execute(VerifyAccessTokenRequest("anytoken")) }
            .returns(VerifyAccessTokenResponse(ANY_AUTHOR_ID, "jkrowling", "jkrowling@gmail.com"))
        every { updateBookChapterService.execute(serviceRequest()) } returns serviceResponse

        RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .header("Authorization", "Bearer anytoken")
            .and()
            .body(updateBookChapterBody())
            .patch("/v1/books/${ANY_BOOK_ID}/chapters/$ANY_CHAPTER_ID")
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
    }

    private fun serviceRequest(status: String = "published") =
        UpdateBookChapterRequest(ANY_AUTHOR_ID, ANY_BOOK_ID, ANY_CHAPTER_ID, status)

    private fun updateBookChapterBody(status: String = "published") =
        """{
          "status": "$status"
        }"""

    companion object {
        private const val ANY_BOOK_ID = "66f86298-bb74-4496-913e-ebc3dc451f1b"
        private const val ANY_CHAPTER_ID = "9e4af2a5-5fdc-4933-b546-4610004f068f"
        private const val ANY_AUTHOR_ID = "0959893a-88cf-465e-b792-43edd8b69e0e"
    }
}
