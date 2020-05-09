package com.readify.readerlibrary.application.service.getreaderpayments

import com.readify.readerlibrary.domain.payment.BookPayment
import com.readify.readerlibrary.domain.payment.ChapterPayment
import com.readify.readerlibrary.domain.payment.Payment
import com.readify.readerlibrary.domain.payment.PaymentRepository
import com.readify.readerlibrary.domain.payment.ReaderId
import java.time.ZonedDateTime

class GetReaderPaymentsService(private val paymentRepository: PaymentRepository) {
    fun execute(request: GetReaderPaymentsRequest): GetReaderPaymentsResponse =
        request.takeIf { request.readerId == request.requesterId }
            ?.let { paymentRepository.findCompletedByReaderId(ReaderId(request.readerId)) }
            ?.map { it.toResponse() }
            ?.let { ReaderPaymentsResponse(it) }
            ?: RequesterAndRequestedReaderAreDifferent

}

private fun Payment.toResponse(): PaymentResponse {
    return when(this) {
        is BookPayment -> PaymentResponse(
            id.value, readerId.value, amount.amount, amount.currency.toString(), bookId.value, null, startedAt,
            completedAt!!, PaymentTypeResponse.BOOK
        )
        is ChapterPayment -> PaymentResponse(
            id.value, readerId.value, amount.amount, amount.currency.toString(), bookId.value, chapterId.value,
            startedAt, completedAt!!, PaymentTypeResponse.CHAPTER
        )
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