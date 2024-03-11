package dev.shorthouse.coinwatch.data.mapper

import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.data.source.local.model.Coin
import dev.shorthouse.coinwatch.data.source.remote.model.CoinApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.CoinsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.CoinsData
import dev.shorthouse.coinwatch.data.preferences.global.Currency
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.Price
import org.junit.Test

class CoinMapperTest {

    // Class under test
    private val coinMapper = CoinMapper()

    private val defaultCurrency = Currency.USD

    @Test
    fun `When coins data null should return empty list`() {
        // Arrange
        val coinsApiModel = CoinsApiModel(
            coinsData = null
        )

        // Act
        val coins = coinMapper.mapApiModelToModel(
            apiModel = coinsApiModel,
            currency = defaultCurrency
        )

        // Assert
        assertThat(coins).isEmpty()
    }

    @Test
    fun `When coins null should return empty list`() {
        // Arrange
        val coinsApiModel = CoinsApiModel(
            coinsData = CoinsData(
                coins = null
            )
        )

        // Act
        val coins = coinMapper.mapApiModelToModel(
            apiModel = coinsApiModel,
            currency = defaultCurrency
        )

        // Assert
        assertThat(coins).isEmpty()
    }

    @Test
    fun `When coins has null list items should filter out these items`() {
        // Arrange
        val coinsApiModel = CoinsApiModel(
            coinsData = CoinsData(
                coins = listOf(
                    null,
                    null,
                    CoinApiModel(
                        id = "Qwsogvtv82FCd",
                        name = "Bitcoin",
                        symbol = "BTC",
                        imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                        currentPrice = "29490.954785191607",
                        priceChangePercentage24h = "2.04"
                    )
                )
            )
        )

        val expectedCoins = listOf(
            Coin(
                id = "Qwsogvtv82FCd",
                name = "Bitcoin",
                symbol = "BTC",
                imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                currentPrice = Price("29490.954785191607"),
                priceChangePercentage24h = Percentage("2.04"),
            )
        )

        // Act
        val coins = coinMapper.mapApiModelToModel(
            apiModel = coinsApiModel,
            currency = defaultCurrency
        )

        // Assert
        assertThat(coins).isEqualTo(expectedCoins)
    }

    @Test
    fun `When coins has null id items should filter out these items`() {
        // Arrange
        val coinsApiModel = CoinsApiModel(
            coinsData = CoinsData(
                coins = listOf(
                    CoinApiModel(
                        id = null,
                        name = "Bitcoin",
                        symbol = "BTC",
                        imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                        currentPrice = "29490.954785191607",
                        priceChangePercentage24h = "2.04",
                    ),
                    CoinApiModel(
                        id = "Qwsogvtv82FCd",
                        name = "Bitcoin",
                        symbol = "BTC",
                        imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                        currentPrice = "29490.954785191607",
                        priceChangePercentage24h = "2.04",
                    ),
                    CoinApiModel(
                        id = null,
                        name = null,
                        symbol = null,
                        imageUrl = null,
                        currentPrice = null,
                        priceChangePercentage24h = null,
                    )
                )
            )
        )

        val expectedCoins = listOf(
            Coin(
                id = "Qwsogvtv82FCd",
                name = "Bitcoin",
                symbol = "BTC",
                imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                currentPrice = Price("29490.954785191607"),
                priceChangePercentage24h = Percentage("2.04"),
            )
        )

        // Act
        val coins = coinMapper.mapApiModelToModel(
            apiModel = coinsApiModel,
            currency = defaultCurrency
        )

        // Assert
        assertThat(coins).isEqualTo(expectedCoins)
    }

    @Test
    fun `When coins has null values should replace with default values`() {
        // Arrange
        val coinsApiModel = CoinsApiModel(
            coinsData = CoinsData(
                coins = listOf(
                    CoinApiModel(
                        id = "Qwsogvtv82FCd",
                        name = null,
                        symbol = null,
                        imageUrl = null,
                        currentPrice = null,
                        priceChangePercentage24h = null,
                    )
                )
            )
        )

        val expectedCoins = listOf(
            Coin(
                id = "Qwsogvtv82FCd",
                name = "",
                symbol = "",
                imageUrl = "",
                currentPrice = Price(null),
                priceChangePercentage24h = Percentage(null),
            )
        )

        // Act
        val coins = coinMapper.mapApiModelToModel(
            apiModel = coinsApiModel,
            currency = defaultCurrency
        )

        // Assert
        assertThat(coins).isEqualTo(expectedCoins)
    }

    @Test
    fun `When coins has valid values should map to model`() {
        // Arrange
        val coinsApiModel = CoinsApiModel(
            coinsData = CoinsData(
                coins = listOf(
                    CoinApiModel(
                        id = "Qwsogvtv82FCd",
                        name = "Bitcoin",
                        symbol = "BTC",
                        imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                        currentPrice = "29342.42354232",
                        priceChangePercentage24h = "-2.04",
                    )
                )
            )
        )

        val expectedCoins = listOf(
            Coin(
                id = "Qwsogvtv82FCd",
                name = "Bitcoin",
                symbol = "BTC",
                imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                currentPrice = Price("29342.42354232"),
                priceChangePercentage24h = Percentage("-2.04"),
            )
        )

        // Act
        val coins = coinMapper.mapApiModelToModel(
            apiModel = coinsApiModel,
            currency = defaultCurrency
        )

        // Assert
        assertThat(coins).isEqualTo(expectedCoins)
    }

    @Test
    fun `When prices list is null should return empty list`() {
        // Arrange
        val coinsApiModel = CoinsApiModel(
            coinsData = CoinsData(
                coins = listOf(
                    CoinApiModel(
                        id = "Qwsogvtv82FCd",
                        name = "Bitcoin",
                        symbol = "BTC",
                        imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                        currentPrice = "29342.42354232",
                        priceChangePercentage24h = "-2.04",
                    )
                )
            )
        )

        val expectedCoins = listOf(
            Coin(
                id = "Qwsogvtv82FCd",
                name = "Bitcoin",
                symbol = "BTC",
                imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                currentPrice = Price("29342.42354232"),
                priceChangePercentage24h = Percentage("-2.04"),
            )
        )

        // Act
        val coins = coinMapper.mapApiModelToModel(
            apiModel = coinsApiModel,
            currency = defaultCurrency
        )

        // Assert
        assertThat(coins).isEqualTo(expectedCoins)
    }

    @Test
    fun `When prices list has null values should filter out these values`() {
        // Arrange
        val coinsApiModel = CoinsApiModel(
            coinsData = CoinsData(
                coins = listOf(
                    CoinApiModel(
                        id = "Qwsogvtv82FCd",
                        name = "Bitcoin",
                        symbol = "BTC",
                        imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                        currentPrice = "29342.42354232",
                        priceChangePercentage24h = "-2.04",
                    )
                )
            )
        )

        val expectedCoins = listOf(
            Coin(
                id = "Qwsogvtv82FCd",
                name = "Bitcoin",
                symbol = "BTC",
                imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                currentPrice = Price("29342.42354232"),
                priceChangePercentage24h = Percentage("-2.04"),
            )
        )

        // Act
        val coins = coinMapper.mapApiModelToModel(
            apiModel = coinsApiModel,
            currency = defaultCurrency
        )

        // Assert
        assertThat(coins).isEqualTo(expectedCoins)
    }

    @Test
    fun `When prices list has negative values should filter out these values`() {
        // Arrange
        val coinsApiModel = CoinsApiModel(
            coinsData = CoinsData(
                coins = listOf(
                    CoinApiModel(
                        id = "Qwsogvtv82FCd",
                        name = "Bitcoin",
                        symbol = "BTC",
                        imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                        currentPrice = "29342.42354232",
                        priceChangePercentage24h = "-2.04",
                    )
                )
            )
        )

        val expectedCoins = listOf(
            Coin(
                id = "Qwsogvtv82FCd",
                name = "Bitcoin",
                symbol = "BTC",
                imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                currentPrice = Price("29342.42354232"),
                priceChangePercentage24h = Percentage("-2.04"),
            )
        )

        // Act
        val coins = coinMapper.mapApiModelToModel(
            apiModel = coinsApiModel,
            currency = defaultCurrency
        )

        // Assert
        assertThat(coins).isEqualTo(expectedCoins)
    }

    @Test
    fun `When coins has valid values with gbp currency should map to model`() {
        // Arrange
        val currency = Currency.GBP

        val coinsApiModel = CoinsApiModel(
            coinsData = CoinsData(
                coins = listOf(
                    CoinApiModel(
                        id = "Qwsogvtv82FCd",
                        name = "Bitcoin",
                        symbol = "BTC",
                        imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                        currentPrice = "29342.42354232",
                        priceChangePercentage24h = "-2.04",
                    )
                )
            )
        )

        val expectedCoins = listOf(
            Coin(
                id = "Qwsogvtv82FCd",
                name = "Bitcoin",
                symbol = "BTC",
                imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                currentPrice = Price("29342.42354232", currency = currency),
                priceChangePercentage24h = Percentage("-2.04"),
            )
        )

        // Act
        val coins = coinMapper.mapApiModelToModel(
            apiModel = coinsApiModel,
            currency = currency
        )

        // Assert
        assertThat(coins).isEqualTo(expectedCoins)
    }

    @Test
    fun `When coins has valid values with eur currency should map to model`() {
        // Arrange
        val currency = Currency.EUR

        val coinsApiModel = CoinsApiModel(
            coinsData = CoinsData(
                coins = listOf(
                    CoinApiModel(
                        id = "Qwsogvtv82FCd",
                        name = "Bitcoin",
                        symbol = "BTC",
                        imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                        currentPrice = "29342.42354232",
                        priceChangePercentage24h = "-2.04",
                    )
                )
            )
        )

        val expectedCoins = listOf(
            Coin(
                id = "Qwsogvtv82FCd",
                name = "Bitcoin",
                symbol = "BTC",
                imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                currentPrice = Price("29342.42354232", currency = currency),
                priceChangePercentage24h = Percentage("-2.04"),
            )
        )

        // Act
        val coins = coinMapper.mapApiModelToModel(
            apiModel = coinsApiModel,
            currency = currency
        )

        // Assert
        assertThat(coins).isEqualTo(expectedCoins)
    }
}
