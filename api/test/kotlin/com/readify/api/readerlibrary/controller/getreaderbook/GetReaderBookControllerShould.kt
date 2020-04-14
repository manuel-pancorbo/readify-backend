package com.readify.api.readerlibrary.controller.getreaderbook

import com.ninjasquad.springmockk.MockkBean
import com.readify.ContractTest
import com.readify.authentication.application.service.verifyaccesstoken.VerifyAccessTokenRequest
import com.readify.authentication.application.service.verifyaccesstoken.VerifyAccessTokenResponse
import com.readify.authentication.application.service.verifyaccesstoken.VerifyAccessTokenService
import com.readify.readerlibrary.application.service.common.LibraryBookResponse
import com.readify.readerlibrary.application.service.common.LibraryBookTypeResponse.WHOLE
import com.readify.readerlibrary.application.service.getreaderbook.BookDoesNotBelongToReader
import com.readify.readerlibrary.application.service.getreaderbook.GetReaderBookRequest
import com.readify.readerlibrary.application.service.getreaderbook.GetReaderBookService
import com.readify.readerlibrary.application.service.getreaderbook.ReaderBookResponse
import com.readify.readerlibrary.application.service.getreaderbook.RequesterAndRequestedReaderAreDifferent
import io.mockk.every
import io.restassured.RestAssured
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Test
import java.util.UUID

class GetReaderBookControllerShould : ContractTest() {
    @MockkBean
    private lateinit var service: GetReaderBookService

    @MockkBean(relaxed = true)
    private lateinit var verifyAccessTokenService: VerifyAccessTokenService

    @Test
    fun `returns unauthorized when user is not authenticated`() {
        RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .get("/v1/readers/$readerId/books/$bookId")
            .then()
            .statusCode(401)
    }

    @Test
    fun `return 404 when reader is requesting the book of another reader`() {
        val serviceRequest = GetReaderBookRequest(anotherReaderId, readerId, bookId)
        every { verifyAccessTokenService.execute(VerifyAccessTokenRequest("anytoken")) }
            .returns(VerifyAccessTokenResponse(anotherReaderId, "jkrowling", "jkrowling@gmail.com"))
        every { service.execute(serviceRequest) } returns RequesterAndRequestedReaderAreDifferent

        RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .header("Authorization", "Bearer anytoken")
            .and()
            .get("/v1/readers/$readerId/books/$bookId")
            .then()
            .statusCode(404)
    }

    @Test
    fun `return 404 when book does not belongs to reader`() {
        val serviceRequest = GetReaderBookRequest(anotherReaderId, readerId, bookId)
        every { verifyAccessTokenService.execute(VerifyAccessTokenRequest("anytoken")) }
            .returns(VerifyAccessTokenResponse(anotherReaderId, "jkrowling", "jkrowling@gmail.com"))
        every { service.execute(serviceRequest) } returns BookDoesNotBelongToReader

        RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .header("Authorization", "Bearer anytoken")
            .and()
            .get("/v1/readers/$readerId/books/$bookId")
            .then()
            .statusCode(404)
    }

    @Test
    fun `return ok with requested book`() {
        val serviceRequest = GetReaderBookRequest(readerId, readerId, bookId)
        val serviceResponse = ReaderBookResponse(LibraryBookResponse(WHOLE, bookId, emptyList()))
        every { verifyAccessTokenService.execute(VerifyAccessTokenRequest("anytoken")) }
            .returns(VerifyAccessTokenResponse(readerId, "jkrowling", "jkrowling@gmail.com"))
        every { service.execute(serviceRequest) } returns serviceResponse

        RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .header("Authorization", "Bearer anytoken")
            .and()
            .get("/v1/readers/$readerId/books/$bookId")
            .then()
            .statusCode(200)
            .body("type", equalTo(WHOLE.toString().toLowerCase()))
            .body("id", equalTo(bookId))
            .body("chapters", equalTo(emptyList<String>()))
    }

    companion object {
        private val readerId = UUID.randomUUID().toString()
        private val anotherReaderId = UUID.randomUUID().toString()
        private val bookId = UUID.randomUUID().toString()
    }
}