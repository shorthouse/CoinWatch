package dev.shorthouse.coinwatch.model

import com.google.common.truth.Truth.assertThat
import java.math.BigDecimal
import org.junit.Test

class PriceTest {

    @Test
    fun `When null input should create empty price`() {
        // Arrange
        val nullPrice: String? = null

        // Act
        val price = Price(nullPrice)

        // Assert
        assertThat(price.amount).isEqualTo(BigDecimal.ZERO)
        assertThat(price.formattedAmount).isEqualTo("$ --")
    }

    @Test
    fun `When empty input should create zero price`() {
        // Arrange
        val emptyPrice = ""

        // Act
        val price = Price(emptyPrice)

        // Assert
        assertThat(price.amount).isEqualTo(BigDecimal.ZERO)
        assertThat(price.formattedAmount).isEqualTo("$0.000000")
    }

    @Test
    fun `When invalid input should create zero price`() {
        // Arrange
        val invalidPrice = "1.x"

        // Act
        val price = Price(invalidPrice)

        // Assert
        assertThat(price.amount).isEqualTo(BigDecimal.ZERO)
        assertThat(price.formattedAmount).isEqualTo("$0.000000")
    }

    @Test
    fun `When valid input should create expected price`() {
        // Arrange
        val validPrice = "1.23"

        // Act
        val price = Price(validPrice)

        // Assert
        assertThat(price.amount).isEqualTo(BigDecimal("1.23"))
        assertThat(price.formattedAmount).isEqualTo("$1.23")
    }

    @Test
    fun `When valid many decimal places input should create expected decimal places price`() {
        // Arrange
        val validPrice = "1.23456789"

        // Act
        val price = Price(validPrice)

        // Assert
        assertThat(price.amount).isEqualTo(BigDecimal("1.23456789"))
        assertThat(price.formattedAmount).isEqualTo("$1.23")
    }

    @Test
    fun `When valid no decimal places input should create expected decimal places price`() {
        // Arrange
        val validPrice = "1"

        // Act
        val price = Price(validPrice)

        // Assert
        assertThat(price.amount).isEqualTo(BigDecimal("1"))
        assertThat(price.formattedAmount).isEqualTo("$1.00")
    }

    @Test
    fun `When valid one decimal place input should create expected decimal places price`() {
        // Arrange
        val validPrice = "1.2"

        // Act
        val price = Price(validPrice)

        // Assert
        assertThat(price.amount).isEqualTo(BigDecimal("1.2"))
        assertThat(price.formattedAmount).isEqualTo("$1.20")
    }

    @Test
    fun `When valid should round up input should create expected rounded price`() {
        // Arrange
        val shouldRoundValidPrice = "1.2391"

        // Act
        val price = Price(shouldRoundValidPrice)

        // Assert
        assertThat(price.amount).isEqualTo(BigDecimal("1.2391"))
        assertThat(price.formattedAmount).isEqualTo("$1.24")
    }

    @Test
    fun `When valid negative input should create expected decimal places price`() {
        // Arrange
        val validPrice = "-1.23242"

        // Act
        val price = Price(validPrice)

        // Assert
        assertThat(price.amount).isEqualTo(BigDecimal("-1.23242"))
        assertThat(price.formattedAmount).isEqualTo("-$1.23")
    }

    @Test
    fun `When valid small positive input should create extra decimal places price`() {
        // Arrange
        val validPrice = "0.000123"

        // Act
        val price = Price(validPrice)

        // Assert
        assertThat(price.amount).isEqualTo(BigDecimal("0.000123"))
        assertThat(price.formattedAmount).isEqualTo("$0.000123")
    }

    @Test
    fun `When valid small negative input should create extra decimal places price`() {
        // Arrange
        val validPrice = "-0.009231735"

        // Act
        val price = Price(validPrice)

        // Assert
        assertThat(price.amount).isEqualTo(BigDecimal("-0.009231735"))
        assertThat(price.formattedAmount).isEqualTo("-$0.009232")
    }

    @Test
    fun `When valid large input should create expected comma formatted price`() {
        // Arrange
        val validPrice = "123456789.12"

        // Act
        val price = Price(validPrice)

        // Assert
        assertThat(price.amount).isEqualTo(BigDecimal("123456789.12"))
        assertThat(price.formattedAmount).isEqualTo("$123,456,789.12")
    }

    @Test
    fun `When valid large input with commas input should be parsed correctly`() {
        // Arrange
        val validPrice = "123,456,789.12"

        // Act
        val price = Price(validPrice)

        // Assert
        assertThat(price.amount).isEqualTo(BigDecimal("123456789.12"))
        assertThat(price.formattedAmount).isEqualTo("$123,456,789.12")
    }

    @Test
    fun `When valid large input with commas and no decimal places should be parsed correctly`() {
        // Arrange
        val validPrice = "123,456,789"

        // Act
        val price = Price(validPrice)

        // Assert
        assertThat(price.amount).isEqualTo(BigDecimal("123456789"))
        assertThat(price.formattedAmount).isEqualTo("$123,456,789.00")
    }

    @Test
    fun `When valid massive input is parsed correctly`() {
        // Arrange
        val massivePrice = "23525782458793458793578905437890054935783245892347580934275093247952349785734290857938245798234578934"

        // Act
        val price = Price(massivePrice)

        // Assert
        assertThat(price.amount).isEqualTo(
            BigDecimal(
                "23525782458793458793578905437890054935783245892347580934275093247952349785734290857938245798234578934"
            )
        )
        assertThat(price.formattedAmount).isEqualTo(
            "$23,525,782,458,793,458,793,578,905,437,890,054,935,783,245,892,347,580,934,275,093,247,952,349,785,734,290,857,938,245,798,234,578,934.00"
        )
    }

    @Test
    fun `When valid input with whitespace and commas should be parsed correctly`() {
        // Arrange
        val validPrice = "   45,678,901.23   "

        // Act
        val price = Price(validPrice)

        // Assert
        assertThat(price.amount).isEqualTo(BigDecimal("45678901.23"))
        assertThat(price.formattedAmount).isEqualTo("$45,678,901.23")
    }
}
