package dev.shorthouse.coinwatch.common

import java.math.BigDecimal

fun String?.toSanitisedBigDecimalOrZero(): BigDecimal {
    return try {
        if (this == null) {
            BigDecimal.ZERO
        } else {
            val sanitisedString = this
                .filterNot { it == ',' }
                .trim()

            BigDecimal(sanitisedString)
        }
    } catch (e: NumberFormatException) {
        BigDecimal.ZERO
    }
}

fun String?.toSanitisedBigDecimalOrNull(): BigDecimal? {
    return try {
        if (this == null) {
            null
        } else {
            val sanitisedString = this
                .filterNot { it == ',' }
                .trim()

            BigDecimal(sanitisedString)
        }
    } catch (e: NumberFormatException) {
        null
    }
}

fun List<BigDecimal>.minOrZero(): BigDecimal {
    return this.minOrNull() ?: BigDecimal.ZERO
}

fun List<BigDecimal>.maxOrZero(): BigDecimal {
    return this.maxOrNull() ?: BigDecimal.ZERO
}

fun String?.toDoubleOrZero(): Double {
    return this?.toDoubleOrNull() ?: 0.0
}
