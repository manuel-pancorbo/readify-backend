package com.readify.readerlibrary.infrastructure.domain.payment

import com.readify.readerlibrary.domain.payment.BookPayment
import com.readify.readerlibrary.domain.payment.BookPaymentAttempt
import com.readify.readerlibrary.domain.payment.ChapterPayment
import com.readify.readerlibrary.domain.payment.ChapterPaymentAttempt
import com.readify.readerlibrary.domain.payment.PaymentAttempt
import com.readify.readerlibrary.domain.payment.PaymentId
import com.readify.readerlibrary.domain.payment.Status

class PaymentFactory {
    fun createPayment(paymentAttempt: PaymentAttempt, paymentId: PaymentId) =
        when (paymentAttempt) {
            is BookPaymentAttempt -> BookPayment(
                paymentId, paymentAttempt.readerId, Status.PENDING, paymentAttempt.getPrice(), paymentAttempt.book.id
            )
            is ChapterPaymentAttempt -> ChapterPayment(
                paymentId, paymentAttempt.readerId, Status.PENDING, paymentAttempt.getPrice(), paymentAttempt.book.id,
                paymentAttempt.chapter.id
            )
        }
}