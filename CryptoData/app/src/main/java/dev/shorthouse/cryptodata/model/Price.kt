package dev.shorthouse.cryptodata.model

import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

data class Price(val amount: Double) {
    companion object {
        private val currencyFormat: NumberFormat =
            NumberFormat.getCurrencyInstance(Locale.US).apply {
                currency = Currency.getInstance("USD")
                minimumFractionDigits = 2
                maximumFractionDigits = 2
            }
    }

    constructor(amount: Long) : this(amount.toDouble())

    val formattedAmount: String = currencyFormat.format(amount)
}
