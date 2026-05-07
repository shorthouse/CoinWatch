package dev.shorthouse.coinwatch.common

import java.math.BigDecimal

fun String?.toSanitisedBigDecimalOrNull(): BigDecimal? {
    return try {
        if (this != null) {
            val sanitisedString = this
                .filterNot { it == ',' }
                .trim()

            BigDecimal(sanitisedString)
        } else {
            null
        }
    } catch (e: NumberFormatException) {
        null
    }
}

fun String?.toSanitisedBigDecimalOrZero(): BigDecimal {
    return toSanitisedBigDecimalOrNull() ?: BigDecimal.ZERO
}
