package com.readify.api.readerlibrary.controller.getreaderpayments

import com.fasterxml.jackson.annotation.JsonProperty
import com.readify.api.bookpublishing.controller.common.HttpMoney
import com.readify.authentication.domain.AnonymousUser
import com.readify.authentication.domain.LoggedUser
import com.readify.authentication.domain.Requester
import com.readify.readerlibrary.application.service.getreaderpayments.GetReaderPaymentsRequest
import com.readify.readerlibrary.application.service.getreaderpayments.GetReaderPaymentsResponse
import com.readify.readerlibrary.application.service.getreaderpayments.GetReaderPaymentsService
import com.readify.readerlibrary.application.service.getreaderpayments.PaymentResponse
import com.readify.readerlibrary.application.service.getreaderpayments.ReaderPaymentsResponse
import com.readify.readerlibrary.application.service.getreaderpayments.RequesterAndRequestedReaderAreDifferent
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.ZonedDateTime

@RestController
@RequestMapping("/v1")
class GetReaderPaymentsController(private val service: GetReaderPaymentsService) {
    @GetMapping("/readers/{readerId}/payments")
    fun getBook(requester: Requester, @PathVariable readerId: String): ResponseEntity<out Any> =
        when (requester) {
            is AnonymousUser -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
            is LoggedUser -> service.execute(GetReaderPaymentsRequest(readerId, requester.id))
                .toHttpResponse()
        }
}

private fun GetReaderPaymentsResponse.toHttpResponse() =
    when (this) {
        RequesterAndRequestedReaderAreDifferent -> ResponseEntity.notFound().build()
        is ReaderPaymentsResponse -> ResponseEntity.ok(
            ReaderPaymentsHttpResponse(this.payments.map { it.toHttpResponse() })
        )
    }

private fun PaymentResponse.toHttpResponse() =
    PaymentHttpResponse(
        id, readerId, HttpMoney(amount, currency), bookId, chapterId, startedAt, completedAt,
        type.toString().toLowerCase()
    )

data class ReaderPaymentsHttpResponse(@JsonProperty("payments") val payments: List<PaymentHttpResponse>)
data class PaymentHttpResponse(
    @JsonProperty("id") val id: String,
    @JsonProperty("reader") val reader: String,
    @JsonProperty("amount") val amount: HttpMoney,
    @JsonProperty("book") val book: String,
    @JsonProperty("chapter") val chapter: String?,
    @JsonProperty("startedAt") val startedAt: ZonedDateTime,
    @JsonProperty("completedAt") val completedAt: ZonedDateTime,
    @JsonProperty("type") val type: String
)
