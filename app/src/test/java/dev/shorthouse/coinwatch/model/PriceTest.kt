package dev.shorthouse.coinwatch.model

import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.data.preferences.global.Currency
import org.junit.Test
import java.math.BigDecimal

class PriceTest {

    @Test
    fun `When null input should create empty price`() {
        // Arrange
        val nullPrice: String? = null

        // Act
        val price = Price(nullPrice)

        // Assert
        assertThat(price.amount).isEqualTo(BigDecimal.ZERO)
        assertThat(price.formattedAmount).isEqualTo("$--")
    }

    @Test
    fun `When empty input should create empty price`() {
        // Arrange
        val emptyPrice = ""

        // Act
        val price = Price(emptyPrice)

        // Assert
        assertThat(price.amount).isEqualTo(BigDecimal.ZERO)
        assertThat(price.formattedAmount).isEqualTo("$--")
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
        assertThat(price.formattedAmount).isEqualTo("$1.000000")
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
        assertThat(price.formattedAmount).isEqualTo("$123.46M")
    }

    @Test
    fun `When valid large input with commas input should be parsed correctly`() {
        // Arrange
        val validPrice = "123,456,789.12"

        // Act
        val price = Price(validPrice)

        // Assert
        assertThat(price.amount).isEqualTo(BigDecimal("123456789.12"))
        assertThat(price.formattedAmount).isEqualTo("$123.46M")
    }

    @Test
    fun `When valid large input with commas and no decimal places should be parsed correctly`() {
        // Arrange
        val validPrice = "123,456,789"

        // Act
        val price = Price(validPrice)

        // Assert
        assertThat(price.amount).isEqualTo(BigDecimal("123456789"))
        assertThat(price.formattedAmount).isEqualTo("$123.46M")
    }

    @Test
    fun `When valid massive input is parsed correctly`() {
        // Arrange
        val massivePrice =
            "23525782458793458793578905437890054935783245892347580934275093247952349785734290857938245798234578934"

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
        assertThat(price.formattedAmount).isEqualTo("$45.68M")
    }

    @Test
    fun `When valid price input with USD currency should be parsed correctly`() {
        // Arrange
        val validPrice = "1.23"
        val currency = Currency.USD

        // Act
        val price = Price(validPrice, currency)

        // Assert
        assertThat(price.amount).isEqualTo(BigDecimal("1.23"))
        assertThat(price.formattedAmount).isEqualTo("$1.23")
    }

    @Test
    fun `When invalid price input with USD currency should be parsed correctly`() {
        // Arrange
        val invalidPrice = null
        val currency = Currency.USD

        // Act
        val price = Price(invalidPrice, currency)

        // Assert
        assertThat(price.amount).isEqualTo(BigDecimal.ZERO)
        assertThat(price.formattedAmount).isEqualTo("$--")
    }

    @Test
    fun `When valid price input with GBP currency should be parsed correctly`() {
        // Arrange
        val validPrice = "1.23"
        val currency = Currency.GBP

        // Act
        val price = Price(validPrice, currency)

        // Assert
        assertThat(price.amount).isEqualTo(BigDecimal("1.23"))
        assertThat(price.formattedAmount).isEqualTo("£1.23")
    }

    @Test
    fun `When invalid price input with GBP currency should be parsed correctly`() {
        // Arrange
        val invalidPrice = null
        val currency = Currency.GBP

        // Act
        val price = Price(invalidPrice, currency)

        // Assert
        assertThat(price.amount).isEqualTo(BigDecimal.ZERO)
        assertThat(price.formattedAmount).isEqualTo("£--")
    }

    @Test
    fun `When valid price input with EUR currency should be parsed correctly`() {
        // Arrange
        val validPrice = "1.23"
        val currency = Currency.EUR

        // Act
        val price = Price(validPrice, currency)

        // Assert
        assertThat(price.amount).isEqualTo(BigDecimal("1.23"))
        assertThat(price.formattedAmount).isEqualTo("€1.23")
    }

    @Test
    fun `When invalid price input with EUR currency should be parsed correctly`() {
        // Arrange
        val invalidPrice = null
        val currency = Currency.EUR

        // Act
        val price = Price(invalidPrice, currency)

        // Assert
        assertThat(price.amount).isEqualTo(BigDecimal.ZERO)
        assertThat(price.formattedAmount).isEqualTo("€--")
    }

    @Test
    fun `When comparing same price different scale should return equal`() {
        // Arrange
        val price1 = "100"
        val price2 = "100.00"

        // Act
        val priceNoDecimals = Price(price1)
        val priceDecimals = Price(price2)

        // Assert
        assertThat(priceNoDecimals.compareTo(priceDecimals)).isEqualTo(0)
    }

    @Test
    fun `When price just below a million should not perform shortening`() {
        val price = "999999.99"

        val normalPrice = Price(price)

        assertThat(normalPrice.amount).isEqualTo(BigDecimal("999999.99"))
        assertThat(normalPrice.formattedAmount).isEqualTo("$999,999.99")
    }

    @Test
    fun `When price just below million no decimal places should not perform shortening`() {
        val price = "999999"

        val normalPrice = Price(price)

        assertThat(normalPrice.amount).isEqualTo(BigDecimal("999999"))
        assertThat(normalPrice.formattedAmount).isEqualTo("$999,999.00")
    }

    @Test
    fun `When price is a million should perform shortening`() {
        val price = "1000000.00"

        val millionPrice = Price(price)

        assertThat(millionPrice.amount).isEqualTo(BigDecimal("1000000.00"))
        assertThat(millionPrice.formattedAmount).isEqualTo("$1.00M")
    }

    @Test
    fun `When price is a million no decimal places should perform shortening`() {
        val price = "1000000"

        val millionPrice = Price(price)

        assertThat(millionPrice.amount).isEqualTo(BigDecimal("1000000"))
        assertThat(millionPrice.formattedAmount).isEqualTo("$1.00M")
    }

    @Test
    fun `When price is random million value should perform shortening`() {
        val price = "47234012"

        val millionPrice = Price(price)

        assertThat(millionPrice.amount).isEqualTo(BigDecimal("47234012"))
        assertThat(millionPrice.formattedAmount).isEqualTo("$47.23M")
    }

    @Test
    fun `When price rounds to under a billion should perform million shortening`() {
        val price = "999994999.99"

        val millionPrice = Price(price)

        assertThat(millionPrice.amount).isEqualTo(BigDecimal("999994999.99"))
        assertThat(millionPrice.formattedAmount).isEqualTo("$999.99M")
    }

    @Test
    fun `When price rounds to under billion no decimal places should perform million shortening`() {
        val price = "999994999"

        val millionPrice = Price(price)

        assertThat(millionPrice.amount).isEqualTo(BigDecimal("999994999"))
        assertThat(millionPrice.formattedAmount).isEqualTo("$999.99M")
    }

    @Test
    fun `When price rounds to a billion should perform billion shortening`() {
        val price = "999995000.00"

        val billionPrice = Price(price)

        assertThat(billionPrice.amount).isEqualTo(BigDecimal("999995000.00"))
        assertThat(billionPrice.formattedAmount).isEqualTo("$1.00B")
    }

    @Test
    fun `When price rounds to a billion no decimal places should perform billion shortening`() {
        val price = "999995000"

        val billionPrice = Price(price)

        assertThat(billionPrice.amount).isEqualTo(BigDecimal("999995000"))
        assertThat(billionPrice.formattedAmount).isEqualTo("$1.00B")
    }

    @Test
    fun `When price is a billion should perform billion shortening`() {
        val price = "1000000000.00"

        val billionPrice = Price(price)

        assertThat(billionPrice.amount).isEqualTo(BigDecimal("1000000000.00"))
        assertThat(billionPrice.formattedAmount).isEqualTo("$1.00B")
    }

    @Test
    fun `When price is a billion no decimal places should perform billion shortening`() {
        val price = "1000000000"

        val billionPrice = Price(price)

        assertThat(billionPrice.amount).isEqualTo(BigDecimal("1000000000"))
        assertThat(billionPrice.formattedAmount).isEqualTo("$1.00B")
    }

    @Test
    fun `When price is a random billion should perform billion shortening`() {
        val price = "139994238103.23"

        val billionPrice = Price(price)

        assertThat(billionPrice.amount).isEqualTo(BigDecimal("139994238103.23"))
        assertThat(billionPrice.formattedAmount).isEqualTo("$139.99B")
    }

    @Test
    fun `When price is a random billion no decimal places should perform billion shortening`() {
        val price = "139994238103"

        val billionPrice = Price(price)

        assertThat(billionPrice.amount).isEqualTo(BigDecimal("139994238103"))
        assertThat(billionPrice.formattedAmount).isEqualTo("$139.99B")
    }

    @Test
    fun `When price rounds to under a trillion should perform billion shortening`() {
        val price = "999994999999.99"

        val billionPrice = Price(price)

        assertThat(billionPrice.amount).isEqualTo(BigDecimal("999994999999.99"))
        assertThat(billionPrice.formattedAmount).isEqualTo("$999.99B")
    }

    @Test
    fun `When price rounds to under trillion no decimal places should perform billion shortening`() {
        val price = "999994999999"

        val billionPrice = Price(price)

        assertThat(billionPrice.amount).isEqualTo(BigDecimal("999994999999"))
        assertThat(billionPrice.formattedAmount).isEqualTo("$999.99B")
    }

    @Test
    fun `When price rounds to trillion should perform trillion shortening`() {
        val price = "999995000000.00"

        val trillionPrice = Price(price)

        assertThat(trillionPrice.amount).isEqualTo(BigDecimal("999995000000.00"))
        assertThat(trillionPrice.formattedAmount).isEqualTo("$1.00T")
    }

    @Test
    fun `When price rounds to trillion no decimal places should perform trillion shortening`() {
        val price = "999995000000"

        val trillionPrice = Price(price)

        assertThat(trillionPrice.amount).isEqualTo(BigDecimal("999995000000"))
        assertThat(trillionPrice.formattedAmount).isEqualTo("$1.00T")
    }

    @Test
    fun `When price is a trillion should perform trillion shortening`() {
        val price = "1000000000000.00"

        val trillionPrice = Price(price)

        assertThat(trillionPrice.amount).isEqualTo(BigDecimal("1000000000000.00"))
        assertThat(trillionPrice.formattedAmount).isEqualTo("$1.00T")
    }

    @Test
    fun `When price is a trillion no decimal places should perform trillion shortening`() {
        val price = "1000000000000"

        val trillionPrice = Price(price)

        assertThat(trillionPrice.amount).isEqualTo(BigDecimal("1000000000000"))
        assertThat(trillionPrice.formattedAmount).isEqualTo("$1.00T")
    }

    @Test
    fun `When price is a random trillion should perform trillion shortening`() {
        val price = "12812423810323.23"

        val trillionPrice = Price(price)

        assertThat(trillionPrice.amount).isEqualTo(BigDecimal("12812423810323.23"))
        assertThat(trillionPrice.formattedAmount).isEqualTo("$12.81T")
    }

    @Test
    fun `When price is a random trillion no decimal places should perform trillion shortening`() {
        val price = "12812423810323"

        val trillionPrice = Price(price)

        assertThat(trillionPrice.amount).isEqualTo(BigDecimal("12812423810323"))
        assertThat(trillionPrice.formattedAmount).isEqualTo("$12.81T")
    }

    @Test
    fun `When price rounds to under a quadrillion should perform trillion shortening`() {
        val price = "999994999999999.99"

        val trillionPrice = Price(price)

        assertThat(trillionPrice.amount).isEqualTo(BigDecimal("999994999999999.99"))
        assertThat(trillionPrice.formattedAmount).isEqualTo("$999.99T")
    }

    @Test
    fun `When price rounds to under quadrillion no decimal places should perform trillion shortening`() {
        val price = "999994999999999"

        val trillionPrice = Price(price)

        assertThat(trillionPrice.amount).isEqualTo(BigDecimal("999994999999999"))
        assertThat(trillionPrice.formattedAmount).isEqualTo("$999.99T")
    }

    @Test
    fun `When price rounds to a quadrillion should perform quadrillion shortening`() {
        val price = "999995000000000.00"

        val quadrillionPrice = Price(price)

        assertThat(quadrillionPrice.amount).isEqualTo(BigDecimal("999995000000000.00"))
        assertThat(quadrillionPrice.formattedAmount).isEqualTo("$1.00Q")
    }

    @Test
    fun `When price rounds to a quadrillion no decimal places should perform quadrillion shortening`() {
        val price = "999995000000000"

        val quadrillionPrice = Price(price)

        assertThat(quadrillionPrice.amount).isEqualTo(BigDecimal("999995000000000"))
        assertThat(quadrillionPrice.formattedAmount).isEqualTo("$1.00Q")
    }

    @Test
    fun `When price is a quadrillion should perform quadrillion shortening`() {
        val price = "1000000000000000.00"

        val quadrillionPrice = Price(price)

        assertThat(quadrillionPrice.amount).isEqualTo(BigDecimal("1000000000000000.00"))
        assertThat(quadrillionPrice.formattedAmount).isEqualTo("$1.00Q")
    }

    @Test
    fun `When price is a quadrillion no decimal places should perform quadrillion shortening`() {
        val price = "1000000000000000"

        val quadrillionPrice = Price(price)

        assertThat(quadrillionPrice.amount).isEqualTo(BigDecimal("1000000000000000"))
        assertThat(quadrillionPrice.formattedAmount).isEqualTo("$1.00Q")
    }

    @Test
    fun `When price is a random quadrillion should perform quadrillion shortening`() {
        val price = "91812812423810323.47"

        val quadrillionPrice = Price(price)

        assertThat(quadrillionPrice.amount).isEqualTo(BigDecimal("91812812423810323.47"))
        assertThat(quadrillionPrice.formattedAmount).isEqualTo("$91.81Q")
    }

    @Test
    fun `When price is a random quadrillion no decimal places should perform quadrillion shortening`() {
        val price = "91812812423810323"

        val quadrillionPrice = Price(price)

        assertThat(quadrillionPrice.amount).isEqualTo(BigDecimal("91812812423810323"))
        assertThat(quadrillionPrice.formattedAmount).isEqualTo("$91.81Q")
    }

    @Test
    fun `When price rounds to under a quintillion should perform quadrillion shortening`() {
        val price = "999994999999999999.99"

        val quadrillionPrice = Price(price)

        assertThat(quadrillionPrice.amount).isEqualTo(BigDecimal("999994999999999999.99"))
        assertThat(quadrillionPrice.formattedAmount).isEqualTo("$999.99Q")
    }

    @Test
    fun `When price rounds to under quintillion no decimal places should perform quadrillion shortening`() {
        val price = "999994999999999999"

        val quadrillionPrice = Price(price)

        assertThat(quadrillionPrice.amount).isEqualTo(BigDecimal("999994999999999999"))
        assertThat(quadrillionPrice.formattedAmount).isEqualTo("$999.99Q")
    }

    @Test
    fun `When price rounds to quintillion should perform no shortening`() {
        val price = "999999500000000000.00"

        val noShortenPrice = Price(price)

        assertThat(noShortenPrice.amount).isEqualTo(BigDecimal("999999500000000000.00"))
        assertThat(noShortenPrice.formattedAmount).isEqualTo("$999,999,500,000,000,000.00")
    }

    @Test
    fun `When price rounds to quintillion no decimal places should perform no shortening`() {
        val price = "999999500000000000"

        val noShortenPrice = Price(price)

        assertThat(noShortenPrice.amount).isEqualTo(BigDecimal("999999500000000000"))
        assertThat(noShortenPrice.formattedAmount).isEqualTo("$999,999,500,000,000,000.00")
    }

    @Test
    fun `When price is a quintillion should perform no shortening`() {
        val price = "1000000000000000000.00"

        val noShortenPrice = Price(price)

        assertThat(noShortenPrice.amount).isEqualTo(BigDecimal("1000000000000000000.00"))
        assertThat(noShortenPrice.formattedAmount).isEqualTo("$1,000,000,000,000,000,000.00")
    }

    @Test
    fun `When price is a quintillion no decimal places should perform no shortening`() {
        val price = "1000000000000000000"

        val noShortenPrice = Price(price)

        assertThat(noShortenPrice.amount).isEqualTo(BigDecimal("1000000000000000000"))
        assertThat(noShortenPrice.formattedAmount).isEqualTo("$1,000,000,000,000,000,000.00")
    }

    @Test
    fun `When price is shortened with GBP currency should shorten correctly`() {
        val price1 = "231,842.94"
        val price2 = "49491394.23440234"
        val price3 = "    1009900243     "
        val price4 = "3084938574102"
        val price5 = "27913084938574102"
        val price6 = "149227913084938574102"

        val normalPrice = Price(price1, Currency.GBP)
        val millionPrice = Price(price2, Currency.GBP)
        val billionPrice = Price(price3, Currency.GBP)
        val trillionPrice = Price(price4, Currency.GBP)
        val quadrillionPrice = Price(price5, Currency.GBP)
        val quintillionPrice = Price(price6, Currency.GBP)

        assertThat(normalPrice.formattedAmount).isEqualTo("£231,842.94")
        assertThat(millionPrice.formattedAmount).isEqualTo("£49.49M")
        assertThat(billionPrice.formattedAmount).isEqualTo("£1.01B")
        assertThat(trillionPrice.formattedAmount).isEqualTo("£3.08T")
        assertThat(quadrillionPrice.formattedAmount).isEqualTo("£27.91Q")
        assertThat(quintillionPrice.formattedAmount).isEqualTo("£149,227,913,084,938,574,102.00")
    }

    @Test
    fun `When price is shortened with EUR currency should shorten correctly`() {
        val price1 = "231,842.94"
        val price2 = "49491394.23440234"
        val price3 = "    1009900243     "
        val price4 = "3084938574102"
        val price5 = "27913084938574102"
        val price6 = "149227913084938574102"

        val normalPrice = Price(price1, Currency.EUR)
        val millionPrice = Price(price2, Currency.EUR)
        val billionPrice = Price(price3, Currency.EUR)
        val trillionPrice = Price(price4, Currency.EUR)
        val quadrillionPrice = Price(price5, Currency.EUR)
        val quintillionPrice = Price(price6, Currency.EUR)

        assertThat(normalPrice.formattedAmount).isEqualTo("€231,842.94")
        assertThat(millionPrice.formattedAmount).isEqualTo("€49.49M")
        assertThat(billionPrice.formattedAmount).isEqualTo("€1.01B")
        assertThat(trillionPrice.formattedAmount).isEqualTo("€3.08T")
        assertThat(quadrillionPrice.formattedAmount).isEqualTo("€27.91Q")
        assertThat(quintillionPrice.formattedAmount).isEqualTo("€149,227,913,084,938,574,102.00")
    }
}
