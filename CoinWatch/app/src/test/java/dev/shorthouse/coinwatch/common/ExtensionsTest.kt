package dev.shorthouse.coinwatch.common

import com.google.common.truth.Truth.assertThat
import java.math.BigDecimal
import org.junit.Test

class ExtensionsTest {
    @Test
    fun `toBigDecimalOrZero with valid number string returns number`() {
        // Arrange
        val input = "123.456"
        val expectedResult = BigDecimal("123.456")

        // Act
        val result = input.toSanitisedBigDecimalOrZero()

        // Assert
        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `toBigDecimalOrZero with null returns zero`() {
        // Arrange
        val input: String? = null
        val expectedResult = BigDecimal.ZERO

        // Act
        val result = input.toSanitisedBigDecimalOrZero()

        // Assert
        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `toBigDecimalOrZero with invalid number string returns zero`() {
        // Arrange
        val input = "abc"
        val expectedResult = BigDecimal.ZERO

        // Act
        val result = input.toSanitisedBigDecimalOrZero()

        // Assert
        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `toBigDecimalOrNull with valid number string returns number`() {
        // Arrange
        val input = "123.456"
        val expectedResult = BigDecimal("123.456")

        // Act
        val result = input.toSanitisedBigDecimalOrNull()

        // Assert
        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `toBigDecimalOrNull with null returns null`() {
        // Arrange
        val input: String? = null

        // Act
        val result = input.toSanitisedBigDecimalOrNull()

        // Assert
        assertThat(result).isNull()
    }

    @Test
    fun `toBigDecimalOrNull with invalid number string returns null`() {
        // Arrange
        val input = "abc"

        // Act
        val result = input.toSanitisedBigDecimalOrNull()

        // Assert
        assertThat(result).isNull()
    }

    @Test
    fun `minOrZero with non empty list returns minimum value`() {
        // Arrange
        val numbers = listOf(
            BigDecimal("5.25"),
            BigDecimal("2.75"),
            BigDecimal("7.50")
        )
        val expectedResult = BigDecimal("2.75")

        // Act
        val result = numbers.minOrZero()

        // Assert
        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `minOrZero with empty list returns zero`() {
        // Arrange
        val input = emptyList<BigDecimal>()
        val expectedResult = BigDecimal.ZERO

        // Act
        val result = input.minOrZero()

        // Assert
        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `maxOrZero with non empty list returns maximum value`() {
        // Arrange
        val numbers = listOf(
            BigDecimal("5.25"),
            BigDecimal("2.75"),
            BigDecimal("7.50")
        )
        val expectedResult = BigDecimal("7.50")

        // Act
        val result = numbers.maxOrZero()

        // Assert
        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `maxOrZero with empty list returns zero`() {
        // Arrange
        val input = emptyList<BigDecimal>()
        val expectedResult = BigDecimal.ZERO

        // Act
        val result = input.maxOrZero()

        // Assert
        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `toDoubleOrZero with valid number string returns number`() {
        // Arrange
        val input = "123.456"
        val expectedResult = 123.456

        // Act
        val result = input.toDoubleOrZero()

        // Assert
        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `toDoubleOrZero with null returns zero`() {
        // Arrange
        val input: String? = null
        val expectedResult = 0.0

        // Act
        val result = input.toDoubleOrZero()

        // Assert
        assertThat(result).isEqualTo(expectedResult)
    }
}
