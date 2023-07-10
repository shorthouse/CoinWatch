package dev.shorthouse.cryptodata.model

import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

data class Price(private val price: BigDecimal?) {
    companion object {
        private val currencyFormat: NumberFormat =
            NumberFormat.getCurrencyInstance(Locale.US).apply {
                currency = Currency.getInstance("USD")
                minimumFractionDigits = 2
                maximumFractionDigits = 2
            }
    }

    constructor(amount: Long) : this(amount.toDouble())
    constructor(amount: Double?) : this(BigDecimal.valueOf(amount ?: 0.0))

    val amountOld: Double = price?.toDouble() ?: 0.0
    val amount: BigDecimal = price ?: BigDecimal.ZERO
    val formattedAmount: String = currencyFormat.format(amount)
}
