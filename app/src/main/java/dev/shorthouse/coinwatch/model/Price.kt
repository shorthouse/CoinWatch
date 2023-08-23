package dev.shorthouse.coinwatch.model

import dev.shorthouse.coinwatch.common.toSanitisedBigDecimalOrZero
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

data class Price(private val price: String?) {
    companion object {
        private val currencyFormat: NumberFormat =
            NumberFormat.getCurrencyInstance(Locale.US).apply {
                currency = Currency.getInstance("USD")
                minimumFractionDigits = 2
                maximumFractionDigits = 2
            }
    }

    val amount: BigDecimal = price.toSanitisedBigDecimalOrZero()

    val formattedAmount: String = currencyFormat.format(amount)
}
