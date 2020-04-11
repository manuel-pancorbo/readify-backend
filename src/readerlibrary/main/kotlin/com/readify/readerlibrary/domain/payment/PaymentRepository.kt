package com.readify.readerlibrary.domain.payment

interface PaymentRepository {
    fun save(payment: Payment)
    fun findById(paymentId: PaymentId): Payment?
    fun findCompletedByReaderId(readerId: ReaderId): List<Payment>
}
