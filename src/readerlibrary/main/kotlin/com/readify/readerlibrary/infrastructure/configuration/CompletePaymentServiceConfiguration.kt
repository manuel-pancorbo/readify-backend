package com.readify.readerlibrary.infrastructure.configuration

import com.readify.readerlibrary.application.service.completepayment.CompletePaymentService
import com.readify.readerlibrary.domain.payment.PaymentRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CompletePaymentServiceConfiguration {
    @Bean
    fun completePaymentService(paymentRepository: PaymentRepository) = CompletePaymentService(paymentRepository)
}