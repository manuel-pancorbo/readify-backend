package com.readify.shared.domain.money

data class Money(val amount: Float, val currency: Currency) {

    fun getAmountAsLong() = amount.times(100).toLong()

    companion object {
        fun of(amount: Float, currency: String) =
            try {
                Money(amount, Currency.valueOf(currency))
            } catch (exception: IllegalArgumentException) {
                throw CurrencyNotSupportedException(currency)
            }
    }
}

enum class Currency { EUR, USD, GBP, CAD }