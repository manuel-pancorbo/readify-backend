package com.readify.api.readerlibrary.controller.createpayment

import com.ninjasquad.springmockk.MockkBean
import com.readify.ContractTest
import com.readify.authentication.application.service.verifyaccesstoken.VerifyAccessTokenRequest
import com.readify.authentication.application.service.verifyaccesstoken.VerifyAccessTokenResponse
import com.readify.authentication.application.service.verifyaccesstoken.VerifyAccessTokenService
import com.readify.readerlibrary.application.service.createpayment.BookNotFoundResponse
import com.readify.readerlibrary.application.service.createpayment.ChapterNotFoundResponse
import com.readify.readerlibrary.application.service.createpayment.CreatePaymentRequest
import com.readify.readerlibrary.application.service.createpayment.CreatePaymentService
import com.readify.readerlibrary.application.service.createpayment.PaymentCreatedResponse
import io.mockk.every
import io.restassured.RestAssured
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import java.util.UUID

class PostPaymentControllerShould : ContractTest() {
    @MockkBean(relaxed = true)
    private lateinit var createPaymentService: CreatePaymentService

    @MockkBean(relaxed = true)
    private lateinit var verifyAccessTokenService: VerifyAccessTokenService

    @Test
    fun `returns unauthorized when user is not authenticated`() {
        RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .body(createPaymentBody())
            .post("/v1/readers/$readerId/payments")
            .then()
            .statusCode(401)
    }


    @Test
    fun `returns 404 when requested book does not exists`() {
        val serviceRequest = CreatePaymentRequest(readerId, bookId, chapterId)
        every { verifyAccessTokenService.execute(VerifyAccessTokenRequest("anytoken")) }
            .returns(VerifyAccessTokenResponse(readerId, "jkrowling", "jkrowling@gmail.com"))
        every { createPaymentService.execute(serviceRequest) } returns BookNotFoundResponse

        RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .header("Authorization", "Bearer anytoken")
            .body(createPaymentBody())
            .post("/v1/readers/$readerId/payments")
            .then()
            .statusCode(404)
    }


    @Test
    fun `returns 404 when requested chapter does not exists`() {
        val serviceRequest = CreatePaymentRequest(readerId, bookId, chapterId)
        every { verifyAccessTokenService.execute(VerifyAccessTokenRequest("anytoken")) }
            .returns(VerifyAccessTokenResponse(readerId, "jkrowling", "jkrowling@gmail.com"))
        every { createPaymentService.execute(serviceRequest) } returns ChapterNotFoundResponse

        RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .header("Authorization", "Bearer anytoken")
            .body(createPaymentBody())
            .post("/v1/readers/$readerId/payments")
            .then()
            .statusCode(404)
    }


    @Test
    fun `returns 200 when payment is created successfully`() {
        val serviceRequest = CreatePaymentRequest(readerId, bookId, chapterId)
        every { verifyAccessTokenService.execute(VerifyAccessTokenRequest("anytoken")) }
            .returns(VerifyAccessTokenResponse(readerId, "jkrowling", "jkrowling@gmail.com"))
        every { createPaymentService.execute(serviceRequest) } returns PaymentCreatedResponse("any-payment-id")

        RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .header("Authorization", "Bearer anytoken")
            .body(createPaymentBody())
            .post("/v1/readers/$readerId/payments")
            .then()
            .statusCode(200)
            .body("id", equalTo("any-payment-id"))
    }

    private fun createPaymentBody() =
        """{
          "book": "$bookId",
          "chapter": "$chapterId"
        }"""

    companion object {
        private val readerId = UUID.randomUUID().toString()
        private val bookId = UUID.randomUUID().toString()
        private val chapterId = UUID.randomUUID().toString()
    }
}