package com.readify.readerlibrary.application.service.completepayment

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.readify.readerlibrary.domain.payment.PaymentId
import com.readify.readerlibrary.domain.payment.PaymentMother
import com.readify.readerlibrary.domain.payment.PaymentRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import java.util.UUID

class CompletePaymentServiceShould {
    private val paymentRepository: PaymentRepository = mockk(relaxed = true)
    private val service = CompletePaymentService(paymentRepository)

    @Test
    fun `return payment not found when requested payment does not exists`() {
        every { paymentRepository.findById(PaymentId(paymentId)) } returns null

        val response = service.execute(CompletePaymentRequest(readerId, paymentId))

        assertThat(response).isEqualTo(PaymentNotFoundResponse)
    }

    @Test
    fun `return payment completed response when payment has been completed with success`() {
        every { paymentRepository.findById(PaymentId(paymentId)) } returns PaymentMother.pendingBookPayment(paymentId)

        val response = service.execute(CompletePaymentRequest(readerId, paymentId))

        assertThat(response).isEqualTo(PaymentCompletedResponse)
        verify { paymentRepository.save(any()) }
    }

    companion object {
        private val readerId = UUID.randomUUID().toString()
        private val paymentId = UUID.randomUUID().toString()
    }
}