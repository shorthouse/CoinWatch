package dev.shorthouse.coinwatch.common

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.math.BigDecimal

class ExtensionsTest {
    @Test
    fun `When toBigDecimalOrZero called with valid string number should return valid number`() {
        // Arrange
        val input = "123.456"
        val expectedResult = BigDecimal("123.456")

        // Act
        val result = input.toSanitisedBigDecimalOrZero()

        // Assert
        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `When toBigDecimalOrZero called with null string should return zero`() {
        // Arrange
        val input: String? = null
        val expectedResult = BigDecimal.ZERO

        // Act
        val result = input.toSanitisedBigDecimalOrZero()

        // Assert
        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `When toBigDecimalOrZero called with invalid string number should return zero`() {
        // Arrange
        val input = "abc"
        val expectedResult = BigDecimal.ZERO

        // Act
        val result = input.toSanitisedBigDecimalOrZero()

        // Assert
        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `When toBigDecimalOrNull called with valid number string should return valid number`() {
        // Arrange
        val input = "123.456"
        val expectedResult = BigDecimal("123.456")

        // Act
        val result = input.toSanitisedBigDecimalOrNull()

        // Assert
        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `When toBigDecimalOrNull called with null string should return zero`() {
        // Arrange
        val input: String? = null

        // Act
        val result = input.toSanitisedBigDecimalOrNull()

        // Assert
        assertThat(result).isNull()
    }

    @Test
    fun `When toBigDecimalOrNull called with invalid number string should return zero`() {
        // Arrange
        val input = "abc"

        // Act
        val result = input.toSanitisedBigDecimalOrNull()

        // Assert
        assertThat(result).isNull()
    }
}
