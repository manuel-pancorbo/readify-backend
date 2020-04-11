package com.readify.readerlibrary.application.service.getreaderpayments

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import com.readify.readerlibrary.domain.payment.PaymentMother
import com.readify.readerlibrary.domain.payment.PaymentRepository
import com.readify.readerlibrary.domain.payment.ReaderId
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import java.util.UUID

class GetReaderPaymentsServiceShould {
    private val paymentRepository: PaymentRepository = mockk(relaxed = true)
    private val service = GetReaderPaymentsService(paymentRepository)

    @Test
    fun `return error when requester is different from requested reader`() {
        val response = service.execute(GetReaderPaymentsRequest(reader, requester))

        assertThat(response).isEqualTo(RequesterAndRequestedReaderAreDifferent)
    }

    @Test
    fun `return list of payments successfully`() {
        val payments = listOf(PaymentMother.completedChapterPayment(paymentId))
        every { paymentRepository.findCompletedByReaderId(ReaderId(reader)) } returns payments

        val response = service.execute(GetReaderPaymentsRequest(reader, reader))

        response as ReaderPaymentsResponse
        assertThat(response.payments).hasSize(payments.size)
        assertThat(response.payments[0].id).isEqualTo(payments[0].id.value)
        assertThat(response.payments[0].readerId).isEqualTo(payments[0].readerId.value)
        assertThat(response.payments[0].amount).isEqualTo(payments[0].amount.amount)
        assertThat(response.payments[0].currency).isEqualTo(payments[0].amount.currency.toString())
        assertThat(response.payments[0].bookId).isEqualTo(payments[0].bookId.value)
        assertThat(response.payments[0].chapterId).isEqualTo(payments[0].chapterId.value)
        assertThat(response.payments[0].startedAt).isEqualTo(payments[0].startedAt)
        assertThat(response.payments[0].completedAt).isEqualTo(payments[0].completedAt)
        assertThat(response.payments[0].type).isEqualTo(PaymentTypeResponse.CHAPTER)
    }

    companion object {
        private val reader = UUID.randomUUID().toString()
        private val requester = UUID.randomUUID().toString()
        private val paymentId = UUID.randomUUID().toString()
    }
}