package dev.shorthouse.coinwatch.model

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.math.BigDecimal

class PercentageTest {

    @Test
    fun `When positive input should have expected signage function outputs`() {
        // Arrange
        val positivePercentage = "1.0"

        // Act
        val percentage = Percentage(positivePercentage)

        // Assert
        assertThat(percentage.isPositive).isTrue()
        assertThat(percentage.isNegative).isFalse()
    }

    @Test
    fun `When negative input should have expected signage function outputs`() {
        // Arrange
        val negativePercentage = "-1.0"

        // Act
        val percentage = Percentage(negativePercentage)

        // Assert
        assertThat(percentage.isPositive).isFalse()
        assertThat(percentage.isNegative).isTrue()
    }

    @Test
    fun `When zero input should have expected signage function outputs`() {
        // Arrange
        val zeroPercentage = "0.0"

        // Act
        val percentage = Percentage(zeroPercentage)

        // Assert
        assertThat(percentage.isPositive).isFalse()
        assertThat(percentage.isNegative).isFalse()
    }

    @Test
    fun `When null input should create zero percentage`() {
        // Arrange
        val nullPercentage: String? = null

        // Act
        val percentage = Percentage(nullPercentage)

        // Assert
        assertThat(percentage.amount).isEqualTo(BigDecimal.ZERO)
        assertThat(percentage.formattedAmount).isEqualTo("+0.00%")
    }

    @Test
    fun `When empty input should create zero percentage`() {
        // Arrange
        val emptyPercentage = ""

        // Act
        val percentage = Percentage(emptyPercentage)

        // Assert
        assertThat(percentage.amount).isEqualTo(BigDecimal.ZERO)
        assertThat(percentage.formattedAmount).isEqualTo("+0.00%")
    }

    @Test
    fun `When invalid input should create zero percentage`() {
        // Arrange
        val invalidPercentage = "1.x"

        // Act
        val percentage = Percentage(invalidPercentage)

        // Assert
        assertThat(percentage.amount).isEqualTo(BigDecimal.ZERO)
        assertThat(percentage.formattedAmount).isEqualTo("+0.00%")
    }

    @Test
    fun `When valid input should create expected percentage`() {
        // Arrange
        val validPercentage = "1.23"

        // Act
        val percentage = Percentage(validPercentage)

        // Assert
        assertThat(percentage.amount).isEqualTo(BigDecimal("1.23"))
        assertThat(percentage.formattedAmount).isEqualTo("+1.23%")
    }

    @Test
    fun `When valid many decimal places input should create expected decimal places percentage`() {
        // Arrange
        val validPercentage = "1.23456789"

        // Act
        val percentage = Percentage(validPercentage)

        // Assert
        assertThat(percentage.amount).isEqualTo(BigDecimal("1.23456789"))
        assertThat(percentage.formattedAmount).isEqualTo("+1.23%")
    }

    @Test
    fun `When valid no decimal places input should create expected decimal places percentage`() {
        // Arrange
        val validPercentage = "1"

        // Act
        val percentage = Percentage(validPercentage)

        // Assert
        assertThat(percentage.amount).isEqualTo(BigDecimal("1"))
        assertThat(percentage.formattedAmount).isEqualTo("+1.00%")
    }

    @Test
    fun `When valid one decimal place input should create expected decimal places percentage`() {
        // Arrange
        val validPercentage = "1.2"

        // Act
        val percentage = Percentage(validPercentage)

        // Assert
        assertThat(percentage.amount).isEqualTo(BigDecimal("1.2"))
        assertThat(percentage.formattedAmount).isEqualTo("+1.20%")
    }

    @Test
    fun `When valid negative input should create expected percentage`() {
        // Arrange
        val validPercentage = "-1.23467"

        // Act
        val percentage = Percentage(validPercentage)

        // Assert
        assertThat(percentage.amount).isEqualTo(BigDecimal("-1.23467"))
        assertThat(percentage.formattedAmount).isEqualTo("-1.23%")
    }

    @Test
    fun `When valid large input should create expected comma formatted percentage`() {
        // Arrange
        val validPercentage = "123456789.123456789"

        // Act
        val percentage = Percentage(validPercentage)

        // Assert
        assertThat(percentage.amount).isEqualTo(BigDecimal("123456789.123456789"))
        assertThat(percentage.formattedAmount).isEqualTo("+123,456,789.12%")
    }

    @Test
    fun `When valid large input with commas input should be parsed correctly`() {
        // Arrange
        val validPercentage = "123,456,789.12"

        // Act
        val percentage = Percentage(validPercentage)

        // Assert
        assertThat(percentage.amount).isEqualTo(BigDecimal("123456789.12"))
        assertThat(percentage.formattedAmount).isEqualTo("+123,456,789.12%")
    }

    @Test
    fun `When valid large input with commas and no decimal places should be parsed correctly`() {
        // Arrange
        val validPercentage = "123,456,789"

        // Act
        val percentage = Percentage(validPercentage)

        // Assert
        assertThat(percentage.amount).isEqualTo(BigDecimal("123456789"))
        assertThat(percentage.formattedAmount).isEqualTo("+123,456,789.00%")
    }

    @Test
    fun `When valid massive input should be parsed correctly`() {
        val massivePercentage = "23525782458793458793578905437890054935783245892347580934275093247952349785734290857938245798234578934"

        // Act
        val percentage = Percentage(massivePercentage)

        // Assert
        assertThat(percentage.amount).isEqualTo(
            BigDecimal(
                "23525782458793458793578905437890054935783245892347580934275093247952349785734290857938245798234578934"
            )
        )
        assertThat(percentage.formattedAmount).isEqualTo(
            "+23,525,782,458,793,458,793,578,905,437,890,054,935,783,245,892,347,580,934,275,093,247,952,349,785,734,290,857,938,245,798,234,578,934.00%"
        )
    }

    @Test
    fun `When valid input with whitespace and commas should be parsed correctly`() {
        // Arrange
        val validPercentage = "        97,123,456.21     "

        // Act
        val percentage = Percentage(validPercentage)

        // Assert
        assertThat(percentage.amount).isEqualTo(BigDecimal("97123456.21"))
        assertThat(percentage.formattedAmount).isEqualTo("+97,123,456.21%")
    }
}
