package com.readify.api.readerlibrary.controller.getreaderpayments

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import com.ninjasquad.springmockk.MockkBean
import com.readify.ContractTest
import com.readify.authentication.application.service.verifyaccesstoken.VerifyAccessTokenRequest
import com.readify.authentication.application.service.verifyaccesstoken.VerifyAccessTokenResponse
import com.readify.authentication.application.service.verifyaccesstoken.VerifyAccessTokenService
import com.readify.readerlibrary.application.service.getreaderpayments.GetReaderPaymentsRequest
import com.readify.readerlibrary.application.service.getreaderpayments.GetReaderPaymentsService
import com.readify.readerlibrary.application.service.getreaderpayments.RequesterAndRequestedReaderAreDifferent
import io.mockk.every
import io.restassured.RestAssured
import org.junit.jupiter.api.Test
import java.time.format.DateTimeFormatter

class GetReaderPaymentsControllerShould : ContractTest() {
    @MockkBean
    private lateinit var service: GetReaderPaymentsService

    @MockkBean(relaxed = true)
    private lateinit var verifyAccessTokenService: VerifyAccessTokenService

    @Test
    fun `returns unauthorized when user is not authenticated`() {
        RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .get("/v1/readers/$readerId/payments")
            .then()
            .statusCode(401)
    }

    @Test
    fun `return 404 when reader is requesting payments of another reader`() {
        val serviceRequest = GetReaderPaymentsRequest(readerId, anotherReaderId)
        every { verifyAccessTokenService.execute(VerifyAccessTokenRequest("anytoken")) }
            .returns(VerifyAccessTokenResponse(anotherReaderId, "jkrowling", "jkrowling@gmail.com"))
        every { service.execute(serviceRequest) } returns RequesterAndRequestedReaderAreDifferent

        RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .header("Authorization", "Bearer anytoken")
            .and()
            .get("/v1/readers/$readerId/payments")
            .then()
            .statusCode(404)
    }

    @Test
    fun `return ok with the list of payments`() {
        val serviceRequest = GetReaderPaymentsRequest(readerId, readerId)
        val serviceResponse = GetReaderPaymentsResponseMother().singleChapterPaymentResponse(readerId)
        every { verifyAccessTokenService.execute(VerifyAccessTokenRequest("anytoken")) }
            .returns(VerifyAccessTokenResponse(readerId, "jkrowling", "jkrowling@gmail.com"))
        every { service.execute(serviceRequest) } returns serviceResponse

        val httpResponse = RestAssured.given()
            .`when`()
            .contentType("application/json")
            .and()
            .header("Authorization", "Bearer anytoken")
            .and()
            .get("/v1/readers/$readerId/payments")
            .then()
            .statusCode(200)
            .extract()
            .response()
            .jsonPath()

        assertThat(httpResponse.getList<String>("payments")).hasSize(1)
        val httpPaymentResponse = httpResponse.getMap<String, String>("payments[0]")
        val httpAmount = httpResponse.getMap<String, String>("payments[0].amount")
        assertThat(httpPaymentResponse["id"]).isEqualTo(serviceResponse.payments[0].id)
        assertThat(httpPaymentResponse["reader"]).isEqualTo(serviceResponse.payments[0].readerId)
        assertThat(httpPaymentResponse["book"]).isEqualTo(serviceResponse.payments[0].bookId)
        assertThat(httpPaymentResponse["chapter"]).isEqualTo(serviceResponse.payments[0].chapterId)
        assertThat(httpPaymentResponse["type"]).isEqualTo(serviceResponse.payments[0].type.toString().toLowerCase())
        assertThat(httpAmount["amount"]).isEqualTo(serviceResponse.payments[0].amount)
        assertThat(httpAmount["currency"]).isEqualTo(serviceResponse.payments[0].currency)
        assertThat(httpPaymentResponse["startedAt"]).isEqualTo(serviceResponse.payments[0].startedAt.format(
            DateTimeFormatter.ISO_OFFSET_DATE_TIME
        ))
        assertThat(httpPaymentResponse["completedAt"]).isEqualTo(serviceResponse.payments[0].completedAt.format(
            DateTimeFormatter.ISO_OFFSET_DATE_TIME
        ))
    }

    companion object {
        private const val readerId = "any-reader-id"
        private const val anotherReaderId = "another-reader-id"
    }
}