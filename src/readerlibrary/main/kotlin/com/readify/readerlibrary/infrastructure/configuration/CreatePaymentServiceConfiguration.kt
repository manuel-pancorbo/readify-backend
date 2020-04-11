package com.readify.readerlibrary.infrastructure.configuration

import com.readify.readerlibrary.application.service.createpayment.CreatePaymentService
import com.readify.readerlibrary.domain.book.BookRepository
import com.readify.readerlibrary.domain.chapter.ChapterRepository
import com.readify.readerlibrary.domain.payment.PaymentProvider
import com.readify.readerlibrary.domain.payment.PaymentRepository
import com.readify.readerlibrary.infrastructure.domain.payment.JpaPaymentRepository
import com.readify.readerlibrary.infrastructure.domain.payment.PaymentFactory
import com.readify.readerlibrary.infrastructure.domain.payment.StripePaymentProvider
import com.readify.readerlibrary.infrastructure.jpa.readerlibrary.ReaderLibraryJpaPaymentDataSource
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CreatePaymentServiceConfiguration {
    @Bean
    fun createPaymentService(
        bookRepository: BookRepository,
        chapterRepository: ChapterRepository,
        paymentProvider: PaymentProvider,
        paymentRepository: PaymentRepository
    ) =
        CreatePaymentService(bookRepository, chapterRepository, paymentProvider, paymentRepository)

    @Bean
    fun paymentProvider(
        paymentFactory: PaymentFactory,
        @Value("\${stripe.domain}") domain: String,
        @Value("\${stripe.success}") successUrl: String,
        @Value("\${stripe.cancel}") cancelUrl: String,
        @Value("\${stripe.secretKey}") secretKey: String
    ) =
        StripePaymentProvider(paymentFactory, domain, successUrl, cancelUrl, secretKey)

    @Bean
    fun paymentFactory() = PaymentFactory()

    @Bean
    fun paymentRepository(dataSource: ReaderLibraryJpaPaymentDataSource) =
        JpaPaymentRepository(dataSource)
}