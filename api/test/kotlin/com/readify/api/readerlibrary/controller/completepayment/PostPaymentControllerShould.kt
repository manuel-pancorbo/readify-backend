package com.readify.api.readerlibrary.controller.completepayment

import com.ninjasquad.springmockk.MockkBean
import com.readify.ContractTest
import com.readify.authentication.application.service.verifyaccesstoken.VerifyAccessTokenRequest
import com.readify.authentication.application.service.verifyaccesstoken.VerifyAccessTokenResponse
import com.readify.authentication.application.service.verifyaccesstoken.VerifyAccessTokenService
import com.readify.readerlibrary.application.service.completepayment.CompletePaymentRequest
import com.readify.readerlibrary.application.service.completepayment.CompletePaymentService
import com.readify.readerlibrary.application.service.completepayment.PaymentCompletedResponse
import com.readify.readerlibrary.application.service.completepayment.PaymentNotFoundResponse
import io.mockk.every
import io.restassured.RestAssured
import org.junit.jupiter.api.Test
import java.util.UUID

class PatchPaymentControllerShould : ContractTest() {
    @MockkBean(relaxed = true)
    private lateinit var completePaymentService: CompletePaymentService

    @MockkBean(relaxed = true)
    private lateinit var verifyAccessTokenService: VerifyAccessTokenService

    @Test
    fun `returns unauthorized when user is not authenticated`() {
        RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .patch("/v1/readers/$readerId/payments/$paymentId")
            .then()
            .statusCode(401)
    }


    @Test
    fun `returns 404 when requested payment does not exists`() {
        val serviceRequest = CompletePaymentRequest(readerId, paymentId)
        every { verifyAccessTokenService.execute(VerifyAccessTokenRequest("anytoken")) }
            .returns(VerifyAccessTokenResponse(readerId, "jkrowling", "jkrowling@gmail.com"))
        every { completePaymentService.execute(serviceRequest) } returns PaymentNotFoundResponse

        RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .header("Authorization", "Bearer anytoken")
            .patch("/v1/readers/$readerId/payments/$paymentId")
            .then()
            .statusCode(404)
    }

    @Test
    fun `returns 200 when requested payment has been completed sucessfully`() {
        val serviceRequest = CompletePaymentRequest(readerId, paymentId)
        every { verifyAccessTokenService.execute(VerifyAccessTokenRequest("anytoken")) }
            .returns(VerifyAccessTokenResponse(readerId, "jkrowling", "jkrowling@gmail.com"))
        every { completePaymentService.execute(serviceRequest) } returns PaymentCompletedResponse

        RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .header("Authorization", "Bearer anytoken")
            .patch("/v1/readers/$readerId/payments/$paymentId")
            .then()
            .statusCode(200)
    }

    companion object {
        private val readerId = UUID.randomUUID().toString()
        private val paymentId = UUID.randomUUID().toString()
    }
}