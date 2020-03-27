package com.readify.shared.domain.money

data class CurrencyNotSupportedException(val currency: String) :
    Throwable(message = "Currency not supported: $currency")