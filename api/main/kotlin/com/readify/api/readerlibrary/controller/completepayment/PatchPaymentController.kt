package com.readify.api.readerlibrary.controller.completepayment

import com.readify.authentication.domain.AnonymousUser
import com.readify.authentication.domain.LoggedUser
import com.readify.authentication.domain.Requester
import com.readify.readerlibrary.application.service.completepayment.CompletePaymentRequest
import com.readify.readerlibrary.application.service.completepayment.CompletePaymentResponse
import com.readify.readerlibrary.application.service.completepayment.CompletePaymentService
import com.readify.readerlibrary.application.service.completepayment.PaymentCompletedResponse
import com.readify.readerlibrary.application.service.completepayment.PaymentNotFoundResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1")
class PatchPaymentController(private val service: CompletePaymentService) {
    @PatchMapping("/readers/{readerId}/payments/{paymentId}")
    fun completePayment(
        requester: Requester,
        @PathVariable readerId: String,
        @PathVariable paymentId: String
    ): ResponseEntity<out Any> =
        when (requester) {
            is AnonymousUser -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
            is LoggedUser -> service.execute(CompletePaymentRequest(readerId, paymentId))
                .toHttpResponse()
        }
}

private fun CompletePaymentResponse.toHttpResponse() = when(this) {
    PaymentNotFoundResponse -> ResponseEntity.notFound().build()
    PaymentCompletedResponse -> ResponseEntity.ok("")
}
