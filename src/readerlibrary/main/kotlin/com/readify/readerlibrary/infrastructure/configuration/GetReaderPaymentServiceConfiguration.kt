package com.readify.readerlibrary.infrastructure.configuration

import com.readify.readerlibrary.application.service.getreaderpayments.GetReaderPaymentsService
import com.readify.readerlibrary.domain.payment.PaymentRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GetReaderPaymentServiceConfiguration {
    @Bean
    fun getReaderPaymentsService(paymentRepository: PaymentRepository) =  GetReaderPaymentsService(paymentRepository)
}