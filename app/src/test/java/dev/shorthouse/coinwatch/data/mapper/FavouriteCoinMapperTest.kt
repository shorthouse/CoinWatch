package dev.shorthouse.coinwatch.data.mapper

import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.data.source.local.model.FavouriteCoin
import dev.shorthouse.coinwatch.data.source.remote.model.FavouriteCoinApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.FavouriteCoinsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.FavouriteCoinsData
import dev.shorthouse.coinwatch.data.userPreferences.Currency
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.Price
import java.math.BigDecimal
import kotlinx.collections.immutable.persistentListOf
import org.junit.Test

class FavouriteCoinMapperTest {

    // Class under test
    private val favouriteCoinMapper = FavouriteCoinMapper()

    private val defaultCurrency = Currency.USD

    @Test
    fun `When favourite coins data null should return empty list`() {
        // Arrange
        val favouriteCoinsApiModel = FavouriteCoinsApiModel(
            favouriteCoinsData = null
        )

        // Act
        val favouriteCoins = favouriteCoinMapper.mapApiModelToModel(
            apiModel = favouriteCoinsApiModel,
            currency = defaultCurrency
        )

        // Assert
        assertThat(favouriteCoins).isEmpty()
    }

    @Test
    fun `When favourite coins null should return empty list`() {
        // Arrange
        val favouriteCoinsApiModel = FavouriteCoinsApiModel(
            favouriteCoinsData = FavouriteCoinsData(
                favouriteCoins = null
            )
        )

        // Act
        val favouriteCoins = favouriteCoinMapper.mapApiModelToModel(
            apiModel = favouriteCoinsApiModel,
            currency = defaultCurrency
        )

        // Assert
        assertThat(favouriteCoins).isEmpty()
    }

    @Test
    fun `When favourite coins has null list items should filter out these items`() {
        // Arrange
        val favouriteCoinsApiModel = FavouriteCoinsApiModel(
            favouriteCoinsData = FavouriteCoinsData(
                favouriteCoins = listOf(
                    null,
                    FavouriteCoinApiModel(
                        id = "Qwsogvtv82FCd",
                        name = "Bitcoin",
                        symbol = "BTC",
                        imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                        currentPrice = "29490.954785191607",
                        priceChangePercentage24h = "2.04",
                        prices24h = emptyList()
                    ),
                    null
                )
            )
        )

        val expectedFavouriteCoins = listOf(
            FavouriteCoin(
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
        val favouriteCoins = favouriteCoinMapper.mapApiModelToModel(
            apiModel = favouriteCoinsApiModel,
            currency = defaultCurrency
        )

        // Assert
        assertThat(favouriteCoins).isEqualTo(expectedFavouriteCoins)
    }

    @Test
    fun `When favourite coins has null id items should filter out these items`() {
        // Arrange
        val favouriteCoinsApiModel = FavouriteCoinsApiModel(
            favouriteCoinsData = FavouriteCoinsData(
                favouriteCoins = listOf(
                    null,
                    FavouriteCoinApiModel(
                        id = "Qwsogvtv82FCd",
                        name = "Bitcoin",
                        symbol = "BTC",
                        imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                        currentPrice = "29490.954785191607",
                        priceChangePercentage24h = "2.04",
                        prices24h = emptyList()
                    ),
                    FavouriteCoinApiModel(
                        id = null,
                        name = "Bitcoin",
                        symbol = "BTC",
                        imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                        currentPrice = "29490.954785191607",
                        priceChangePercentage24h = "2.04",
                        prices24h = emptyList()
                    ),
                    FavouriteCoinApiModel(
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

        val expectedFavouriteCoins = listOf(
            FavouriteCoin(
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
        val favouriteCoins = favouriteCoinMapper.mapApiModelToModel(
            apiModel = favouriteCoinsApiModel,
            currency = defaultCurrency
        )

        // Assert
        assertThat(favouriteCoins).isEqualTo(expectedFavouriteCoins)
    }

    @Test
    fun `When favourite coins have null values should replace with default values`() {
        // Arrange
        val favouriteCoinsApiModel = FavouriteCoinsApiModel(
            favouriteCoinsData = FavouriteCoinsData(
                favouriteCoins = listOf(
                    FavouriteCoinApiModel(
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

        val expectedFavouriteCoins = listOf(
            FavouriteCoin(
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
        val favouriteCoins = favouriteCoinMapper.mapApiModelToModel(
            apiModel = favouriteCoinsApiModel,
            currency = defaultCurrency
        )

        // Assert
        assertThat(favouriteCoins).isEqualTo(expectedFavouriteCoins)
    }

    @Test
    fun `When prices list has null values should filter out these values`() {
        // Arrange
        val favouriteCoinsApiModel = FavouriteCoinsApiModel(
            favouriteCoinsData = FavouriteCoinsData(
                favouriteCoins = listOf(
                    FavouriteCoinApiModel(
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

        val expectedFavouriteCoins = listOf(
            FavouriteCoin(
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
        val favouriteCoins = favouriteCoinMapper.mapApiModelToModel(
            apiModel = favouriteCoinsApiModel,
            currency = defaultCurrency
        )

        // Assert
        assertThat(favouriteCoins).isEqualTo(expectedFavouriteCoins)
    }

    @Test
    fun `When prices list has negative values should filter out these values`() {
        // Arrange
        val favouriteCoinsApiModel = FavouriteCoinsApiModel(
            favouriteCoinsData = FavouriteCoinsData(
                favouriteCoins = listOf(
                    FavouriteCoinApiModel(
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

        val expectedFavouriteCoins = listOf(
            FavouriteCoin(
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
        val favouriteCoins = favouriteCoinMapper.mapApiModelToModel(
            apiModel = favouriteCoinsApiModel,
            currency = defaultCurrency
        )

        // Assert
        assertThat(favouriteCoins).isEqualTo(expectedFavouriteCoins)
    }

    @Test
    fun `When favourite coins has valid values with usd currency should map to model`() {
        // Arrange
        val favouriteCoinsApiModel = FavouriteCoinsApiModel(
            favouriteCoinsData = FavouriteCoinsData(
                favouriteCoins = listOf(
                    FavouriteCoinApiModel(
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

        val expectedFavouriteCoins = listOf(
            FavouriteCoin(
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
        val favouriteCoins = favouriteCoinMapper.mapApiModelToModel(
            apiModel = favouriteCoinsApiModel,
            currency = defaultCurrency
        )

        // Assert
        assertThat(favouriteCoins).isEqualTo(expectedFavouriteCoins)
    }

    @Test
    fun `When favourite coins has valid values with gbp currency should map to model`() {
        // Arrange
        val currency = Currency.GBP

        val favouriteCoinsApiModel = FavouriteCoinsApiModel(
            favouriteCoinsData = FavouriteCoinsData(
                favouriteCoins = listOf(
                    FavouriteCoinApiModel(
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

        val expectedFavouriteCoins = listOf(
            FavouriteCoin(
                id = "Qwsogvtv82FCd",
                name = "Bitcoin",
                symbol = "BTC",
                imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                currentPrice = Price("29342.42354232", currency = currency),
                priceChangePercentage24h = Percentage("-2.04"),
                prices24h = persistentListOf(
                    BigDecimal("29490.954785191607"),
                    BigDecimal("29430.31478048720"),
                    BigDecimal("27403.23070285280752480754")
                )
            )
        )

        // Act
        val favouriteCoins = favouriteCoinMapper.mapApiModelToModel(
            apiModel = favouriteCoinsApiModel,
            currency = currency
        )

        // Assert
        assertThat(favouriteCoins).isEqualTo(expectedFavouriteCoins)
    }

    @Test
    fun `When favourite coins has valid values with eur currency should map to model`() {
        // Arrange
        val currency = Currency.EUR

        val favouriteCoinsApiModel = FavouriteCoinsApiModel(
            favouriteCoinsData = FavouriteCoinsData(
                favouriteCoins = listOf(
                    FavouriteCoinApiModel(
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

        val expectedFavouriteCoins = listOf(
            FavouriteCoin(
                id = "Qwsogvtv82FCd",
                name = "Bitcoin",
                symbol = "BTC",
                imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                currentPrice = Price("29342.42354232", currency = currency),
                priceChangePercentage24h = Percentage("-2.04"),
                prices24h = persistentListOf(
                    BigDecimal("29490.954785191607"),
                    BigDecimal("29430.31478048720"),
                    BigDecimal("27403.23070285280752480754")
                )
            )
        )

        // Act
        val favouriteCoins = favouriteCoinMapper.mapApiModelToModel(
            apiModel = favouriteCoinsApiModel,
            currency = currency
        )

        // Assert
        assertThat(favouriteCoins).isEqualTo(expectedFavouriteCoins)
    }
}
