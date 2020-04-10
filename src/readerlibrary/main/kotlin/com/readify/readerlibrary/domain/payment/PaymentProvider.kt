package com.readify.readerlibrary.domain.payment

interface PaymentProvider {
    fun start(paymentAttempt: PaymentAttempt): Payment
}