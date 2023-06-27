package dev.shorthouse.cryptodata.model

import java.text.NumberFormat
import java.util.Locale

data class Percentage(val amount: Double) {
    private val percentageFormat: NumberFormat = NumberFormat.getPercentInstance(Locale.US).apply {
        isGroupingUsed = true
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }

    val formattedAmount: String = if (amount >= 0) {
        "+" + percentageFormat.format(amount / 100)
    } else {
        percentageFormat.format(amount / 100)
    }
}
