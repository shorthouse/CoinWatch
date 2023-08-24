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

        private val currencyFormatSmallPrice: NumberFormat =
            NumberFormat.getCurrencyInstance(Locale.US).apply {
                currency = Currency.getInstance("USD")
                minimumFractionDigits = 6
                maximumFractionDigits = 6
            }
    }

    val amount: BigDecimal = price.toSanitisedBigDecimalOrZero()

    val formattedAmount: String =
        if (amount < BigDecimal("1.00") && amount > BigDecimal("-1.00")) {
            currencyFormatSmallPrice.format(amount)
        } else {
            currencyFormat.format(amount)
        }
}
