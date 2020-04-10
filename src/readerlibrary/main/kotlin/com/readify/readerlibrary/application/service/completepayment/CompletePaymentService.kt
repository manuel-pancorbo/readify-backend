package com.readify.readerlibrary.application.service.completepayment

class CompletePaymentService {
    fun execute(request: CompletePaymentRequest): CompletePaymentResponse {
        TODO("Not yet implemented")
    }

}

data class CompletePaymentRequest(val readerId: String, val paymentId: String)
sealed class CompletePaymentResponse
object PaymentNotFoundResponse: CompletePaymentResponse()
object PaymentCompletedResponse: CompletePaymentResponse()