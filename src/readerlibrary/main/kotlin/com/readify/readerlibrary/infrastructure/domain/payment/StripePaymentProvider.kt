package com.readify.readerlibrary.infrastructure.domain.payment

import com.readify.readerlibrary.domain.book.Book
import com.readify.readerlibrary.domain.chapter.Chapter
import com.readify.readerlibrary.domain.payment.Payment
import com.readify.readerlibrary.domain.payment.PaymentAttempt
import com.readify.readerlibrary.domain.payment.PaymentId
import com.readify.readerlibrary.domain.payment.PaymentProvider
import com.stripe.Stripe
import com.stripe.model.checkout.Session
import com.stripe.param.checkout.SessionCreateParams
import com.stripe.param.checkout.SessionCreateParams.PaymentMethodType

class StripePaymentProvider(
    private val paymentFactory: PaymentFactory,
    private val domain: String,
    private val successUrl: String,
    private val cancelUrl: String,
    private val secretKey: String
) :
    PaymentProvider {
    override fun start(paymentAttempt: PaymentAttempt): Payment {
        Stripe.apiKey = secretKey
        return Session.create(
            SessionCreateParams.Builder()
                .setSuccessUrl("$domain/$successUrl")
                .setCancelUrl("$domain/$cancelUrl")
                .addPaymentMethodType(PaymentMethodType.CARD)
                .addLineItem(
                    SessionCreateParams.LineItem.Builder()
                        .setName(paymentAttempt.getPaymentDescription())
                        .setAmount(paymentAttempt.getPrice().getAmountAsLong())
                        .setCurrency(paymentAttempt.getPrice().currency.toString())
                        .addImage(paymentAttempt.getPaymentImage())
                        .setQuantity(1)
                        .build()
                )
                .build()
        )
            .let { paymentFactory.createPayment(paymentAttempt, PaymentId(it.id)) }
    }
}