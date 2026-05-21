package dev.shorthouse.coinwatch.model

import dev.shorthouse.coinwatch.common.Constants.MISSING_VALUE_PLACEHOLDER
import dev.shorthouse.coinwatch.common.toSanitisedBigDecimalOrNull
import dev.shorthouse.coinwatch.data.source.local.datastore.global.Currency
import java.math.BigDecimal

data class Price(val price: String?, val currency: Currency = Currency.USD) : Comparable<Price> {

    val amount: BigDecimal = price.toSanitisedBigDecimalOrNull() ?: BigDecimal.ZERO

    val formattedAmount: String = formatAmount()

    private fun formatAmount(): String {
        val amountToFormat = price.toSanitisedBigDecimalOrNull()

        return if (amountToFormat == null) {
            "${currency.symbol}$MISSING_VALUE_PLACEHOLDER"
        } else {
            PriceFormatter.format(
                amount = amountToFormat,
                currency = currency
            )
        }
    }

    override fun compareTo(other: Price) = amount.compareTo(other.amount)
}
