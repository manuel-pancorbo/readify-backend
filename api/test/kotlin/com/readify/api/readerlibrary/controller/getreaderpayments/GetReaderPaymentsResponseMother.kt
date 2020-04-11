package com.readify.api.readerlibrary.controller.getreaderpayments

import com.readify.readerlibrary.application.service.getreaderpayments.PaymentResponse
import com.readify.readerlibrary.application.service.getreaderpayments.PaymentTypeResponse
import com.readify.readerlibrary.application.service.getreaderpayments.ReaderPaymentsResponse
import com.readify.shared.domain.clock.Clock
import java.util.UUID

class GetReaderPaymentsResponseMother {
    fun singleChapterPaymentResponse(readerId: String) =
        ReaderPaymentsResponse(
            listOf(
                PaymentResponse(
                    UUID.randomUUID().toString(),
                    readerId,
                    3.99f,
                    "EUR",
                    UUID.randomUUID().toString(),
                    UUID.randomUUID().toString(),
                    Clock().now(),
                    Clock().now(),
                    PaymentTypeResponse.CHAPTER
                )
            )
        )
}
