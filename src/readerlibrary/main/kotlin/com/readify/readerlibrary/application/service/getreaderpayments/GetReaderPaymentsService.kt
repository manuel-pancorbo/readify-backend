package com.readify.readerlibrary.application.service.getreaderpayments

import java.time.ZonedDateTime

class GetReaderPaymentsService {
    fun execute(request: GetReaderPaymentsRequest): GetReaderPaymentsResponse {
        TODO("Not yet implemented")
    }

}

data class GetReaderPaymentsRequest(val readerId: String, val requesterId: String)
sealed class GetReaderPaymentsResponse
object RequesterAndRequestedReaderAreDifferent : GetReaderPaymentsResponse()
data class ReaderPaymentsResponse(val payments: List<PaymentResponse>) : GetReaderPaymentsResponse()
data class PaymentResponse(
    val id: String,
    val readerId: String,
    val amount: Float,
    val currency: String,
    val bookId: String,
    val chapterId: String?,
    val startedAt: ZonedDateTime,
    val completedAt: ZonedDateTime,
    val type: PaymentTypeResponse
)

enum class PaymentTypeResponse { BOOK, CHAPTER }