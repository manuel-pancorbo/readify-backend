package com.readify.readerlibrary.application.service.completepayment

import com.readify.readerlibrary.domain.payment.PaymentId
import com.readify.readerlibrary.domain.payment.PaymentRepository
import com.readify.shared.domain.event.bus.EventBus

class CompletePaymentService(private val paymentRepository: PaymentRepository, private val eventBus: EventBus) {
    fun execute(request: CompletePaymentRequest) =
        paymentRepository.findById(PaymentId(request.paymentId))
            ?.also { paymentRepository.save(it.complete()) }
            ?.also { eventBus.publish(it.pullDomainEvents()) }
            ?.let { PaymentCompletedResponse }
            ?: PaymentNotFoundResponse

}

data class CompletePaymentRequest(val readerId: String, val paymentId: String)
sealed class CompletePaymentResponse
object PaymentNotFoundResponse : CompletePaymentResponse()
object PaymentCompletedResponse : CompletePaymentResponse()