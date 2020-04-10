package com.readify.readerlibrary.infrastructure.configuration

import com.readify.readerlibrary.application.service.createpayment.CreatePaymentService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CreatePaymentServiceConfiguration {
    @Bean
    fun createPaymentService() = CreatePaymentService()
}