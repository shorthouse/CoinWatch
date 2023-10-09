package dev.shorthouse.coinwatch.data.mapper

import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.data.source.remote.model.CoinApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.CoinsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.CoinsData
import dev.shorthouse.coinwatch.model.Coin
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.Price
import java.math.BigDecimal
import kotlinx.collections.immutable.persistentListOf
import org.junit.Test

class CoinMapperTest {

    // Class under test
    private val coinMapper = CoinMapper()

    @Test
    fun `When coins data null should return empty list`() {
        // Arrange
        val apiModel = CoinsApiModel(
            coinsData = null
        )

        // Act
        val coins = coinMapper.mapApiModelToModel(apiModel)

        // Assert
        assertThat(coins).isEmpty()
    }

    @Test
    fun `When coins null should return empty list`() {
        // Arrange
        val apiModel = CoinsApiModel(
            coinsData = CoinsData(
                coins = null
            )
        )

        // Act
        val coins = coinMapper.mapApiModelToModel(apiModel)

        // Assert
        assertThat(coins).isEmpty()
    }

    @Test
    fun `When coins has null list items should filter out these items`() {
        // Arrange
        val apiModel = CoinsApiModel(
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
                        priceChangePercentage24h = "2.04",
                        prices24h = emptyList()
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
                prices24h = persistentListOf()
            )
        )

        // Act
        val coins = coinMapper.mapApiModelToModel(apiModel)

        // Assert
        assertThat(coins).isEqualTo(expectedCoins)
    }

    @Test
    fun `When coins has null id items should filter out these items`() {
        // Arrange
        val apiModel = CoinsApiModel(
            coinsData = CoinsData(
                coins = listOf(
                    CoinApiModel(
                        id = null,
                        name = "Bitcoin",
                        symbol = "BTC",
                        imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                        currentPrice = "29490.954785191607",
                        priceChangePercentage24h = "2.04",
                        prices24h = emptyList()
                    ),
                    CoinApiModel(
                        id = "Qwsogvtv82FCd",
                        name = "Bitcoin",
                        symbol = "BTC",
                        imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                        currentPrice = "29490.954785191607",
                        priceChangePercentage24h = "2.04",
                        prices24h = emptyList()
                    ),
                    CoinApiModel(
                        id = null,
                        name = null,
                        symbol = null,
                        imageUrl = null,
                        currentPrice = null,
                        priceChangePercentage24h = null,
                        prices24h = null
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
                prices24h = persistentListOf()
            )
        )

        // Act
        val coins = coinMapper.mapApiModelToModel(apiModel)

        // Assert
        assertThat(coins).isEqualTo(expectedCoins)
    }

    @Test
    fun `When coins has null values should replace with default values`() {
        // Arrange
        val apiModel = CoinsApiModel(
            coinsData = CoinsData(
                coins = listOf(
                    CoinApiModel(
                        id = "Qwsogvtv82FCd",
                        name = null,
                        symbol = null,
                        imageUrl = null,
                        currentPrice = null,
                        priceChangePercentage24h = null,
                        prices24h = null
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
                prices24h = persistentListOf()
            )
        )

        // Act
        val coins = coinMapper.mapApiModelToModel(apiModel)

        // Assert
        assertThat(coins).isEqualTo(expectedCoins)
    }

    @Test
    fun `When coins has valid values should map to model`() {
        // Arrange
        val apiModel = CoinsApiModel(
            coinsData = CoinsData(
                coins = listOf(
                    CoinApiModel(
                        id = "Qwsogvtv82FCd",
                        name = "Bitcoin",
                        symbol = "BTC",
                        imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                        currentPrice = "29342.42354232",
                        priceChangePercentage24h = "-2.04",
                        prices24h = listOf(
                            BigDecimal("29490.954785191607"),
                            BigDecimal("29430.31478048720"),
                            BigDecimal("27403.23070285280752480754")
                        )
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
                prices24h = persistentListOf(
                    BigDecimal("29490.954785191607"),
                    BigDecimal("29430.31478048720"),
                    BigDecimal("27403.23070285280752480754")
                )
            )
        )

        // Act
        val coins = coinMapper.mapApiModelToModel(apiModel)

        // Assert
        assertThat(coins).isEqualTo(expectedCoins)
    }

    @Test
    fun `When prices list is null should return empty list`() {
        // Arrange
        val apiModel = CoinsApiModel(
            coinsData = CoinsData(
                coins = listOf(
                    CoinApiModel(
                        id = "Qwsogvtv82FCd",
                        name = "Bitcoin",
                        symbol = "BTC",
                        imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                        currentPrice = "29342.42354232",
                        priceChangePercentage24h = "-2.04",
                        prices24h = null
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
                prices24h = persistentListOf()
            )
        )

        // Act
        val coins = coinMapper.mapApiModelToModel(apiModel)

        // Assert
        assertThat(coins).isEqualTo(expectedCoins)
    }

    @Test
    fun `When prices list has null values should filter out these values`() {
        // Arrange
        val apiModel = CoinsApiModel(
            coinsData = CoinsData(
                coins = listOf(
                    CoinApiModel(
                        id = "Qwsogvtv82FCd",
                        name = "Bitcoin",
                        symbol = "BTC",
                        imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                        currentPrice = "29342.42354232",
                        priceChangePercentage24h = "-2.04",
                        prices24h = listOf(
                            null,
                            null,
                            BigDecimal("29490.954785191607"),
                            null,
                            BigDecimal("29430.31478048720"),
                            null,
                            BigDecimal("27403.23070285280752480754"),
                            null
                        )
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
                prices24h = persistentListOf(
                    BigDecimal("29490.954785191607"),
                    BigDecimal("29430.31478048720"),
                    BigDecimal("27403.23070285280752480754")
                )
            )
        )

        // Act
        val coins = coinMapper.mapApiModelToModel(apiModel)

        // Assert
        assertThat(coins).isEqualTo(expectedCoins)
    }

    @Test
    fun `When prices list has negative values should filter out these values`() {
        // Arrange
        val apiModel = CoinsApiModel(
            coinsData = CoinsData(
                coins = listOf(
                    CoinApiModel(
                        id = "Qwsogvtv82FCd",
                        name = "Bitcoin",
                        symbol = "BTC",
                        imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                        currentPrice = "29342.42354232",
                        priceChangePercentage24h = "-2.04",
                        prices24h = listOf(
                            BigDecimal("-29490.954785191607"),
                            BigDecimal("-29430.31478048720"),
                            BigDecimal("29490.954785191607"),
                            BigDecimal("-27403.23070285280752480754")
                        )
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
                prices24h = persistentListOf(
                    BigDecimal("29490.954785191607")
                )
            )
        )

        // Act
        val coins = coinMapper.mapApiModelToModel(apiModel)

        // Assert
        assertThat(coins).isEqualTo(expectedCoins)
    }
}
