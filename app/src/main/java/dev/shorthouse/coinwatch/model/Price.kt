package dev.shorthouse.coinwatch.model

import dev.shorthouse.coinwatch.common.toSanitisedBigDecimalOrNull
import dev.shorthouse.coinwatch.data.source.local.datastore.global.Currency
import java.math.BigDecimal
import java.util.Locale

data class Price(val price: String?, val currency: Currency = Currency.USD) : Comparable<Price> {

    val amount: BigDecimal
        get() = price.toSanitisedBigDecimalOrNull() ?: BigDecimal.ZERO

    val formattedAmount: String
        get() = formatAmount()

    private fun formatAmount(): String {
        val formatLocale = Locale.getDefault(Locale.Category.FORMAT)
        val amountToFormat = price.toSanitisedBigDecimalOrNull()

        return if (amountToFormat == null) {
            PriceFormatter.formatMissing(currency = currency, formatLocale = formatLocale)
        } else {
            PriceFormatter.format(
                amount = amountToFormat,
                currency = currency,
                formatLocale = formatLocale
            )
        }
    }

    override fun compareTo(other: Price) = amount.compareTo(other.amount)
}
