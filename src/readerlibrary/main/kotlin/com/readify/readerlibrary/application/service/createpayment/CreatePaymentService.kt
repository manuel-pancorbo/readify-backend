package com.readify.readerlibrary.application.service.createpayment

class CreatePaymentService {
    fun execute(request: CreatePaymentRequest): CreatePaymentResponse {
        TODO("Not yet implemented")
    }

}

data class CreatePaymentRequest(val readerId: String, val bookId: String, val chapterId: String)
sealed class CreatePaymentResponse
object BookNotFoundResponse: CreatePaymentResponse()
object ChapterNotFoundResponse: CreatePaymentResponse()
data class PaymentCreatedResponse(val id: String): CreatePaymentResponse()