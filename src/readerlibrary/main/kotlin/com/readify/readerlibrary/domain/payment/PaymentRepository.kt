package com.readify.readerlibrary.domain.payment

interface PaymentRepository {
    fun save(payment: Payment)
}