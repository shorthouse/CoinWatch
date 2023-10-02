package dev.shorthouse.coinwatch.common

import java.math.BigDecimal

fun String?.toSanitisedBigDecimalOrZero(): BigDecimal {
    return try {
        if (this != null) {
            val sanitisedString = this
                .filterNot { it == ',' }
                .trim()

            BigDecimal(sanitisedString)
        } else {
            BigDecimal.ZERO
        }
    } catch (e: NumberFormatException) {
        BigDecimal.ZERO
    }
}

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

fun List<BigDecimal>.minOrZero(): BigDecimal {
    return this.minOrNull() ?: BigDecimal.ZERO
}

fun List<BigDecimal>.maxOrZero(): BigDecimal {
    return this.maxOrNull() ?: BigDecimal.ZERO
}
