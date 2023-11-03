package dev.shorthouse.coinwatch.data.mapper

import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.data.datastore.Currency
import dev.shorthouse.coinwatch.data.source.remote.model.AllTimeHigh
import dev.shorthouse.coinwatch.data.source.remote.model.CoinDetailsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.CoinDetailsData
import dev.shorthouse.coinwatch.data.source.remote.model.CoinDetailsDataHolder
import dev.shorthouse.coinwatch.data.source.remote.model.Supply
import dev.shorthouse.coinwatch.model.CoinDetails
import dev.shorthouse.coinwatch.model.Price
import org.junit.Test

class CoinDetailsMapperTest {

    // Class under test
    private val coinDetailsMapper = CoinDetailsMapper()

    private val currency = Currency.USD

    @Test
    fun `When coin details data holder is null should return default values`() {
        // Arrange
        val coinDetailsApiModel = CoinDetailsApiModel(
            coinDetailsDataHolder = null
        )

        val expectedCoinDetails = CoinDetails(
            id = "",
            name = "",
            symbol = "",
            imageUrl = "",
            currentPrice = Price(null),
            marketCap = Price(null),
            marketCapRank = "",
            volume24h = "",
            circulatingSupply = "",
            allTimeHigh = Price(null),
            allTimeHighDate = "",
            listedDate = ""
        )

        // Act
        val coinDetails = coinDetailsMapper.mapApiModelToModel(
            apiModel = coinDetailsApiModel,
            currency = currency
        )

        // Assert
        assertThat(coinDetails).isEqualTo(expectedCoinDetails)
    }

    @Test
    fun `When coin details data is null should return default values`() {
        // Arrange
        val coinDetailsApiModel = CoinDetailsApiModel(
            coinDetailsDataHolder = CoinDetailsDataHolder(
                coinDetailsData = null
            )
        )

        val expectedCoinDetails = CoinDetails(
            id = "",
            name = "",
            symbol = "",
            imageUrl = "",
            currentPrice = Price(null),
            marketCap = Price(null),
            marketCapRank = "",
            volume24h = "",
            circulatingSupply = "",
            allTimeHigh = Price(null),
            allTimeHighDate = "",
            listedDate = ""
        )

        // Act
        val coinDetails = coinDetailsMapper.mapApiModelToModel(
            apiModel = coinDetailsApiModel,
            currency = currency
        )

        // Assert
        assertThat(coinDetails).isEqualTo(expectedCoinDetails)
    }

    @Test
    fun `When all coin details values are null should return default values`() {
        // Arrange
        val coinDetailsApiModel = CoinDetailsApiModel(
            coinDetailsDataHolder = CoinDetailsDataHolder(
                coinDetailsData = CoinDetailsData(
                    id = null,
                    name = null,
                    symbol = null,
                    imageUrl = null,
                    currentPrice = null,
                    marketCap = null,
                    marketCapRank = null,
                    volume24h = null,
                    supply = null,
                    allTimeHigh = null,
                    listedAt = null
                )
            )
        )

        val expectedCoinDetails = CoinDetails(
            id = "",
            name = "",
            symbol = "",
            imageUrl = "",
            currentPrice = Price(null),
            marketCap = Price(null),
            marketCapRank = "",
            volume24h = "",
            circulatingSupply = "",
            allTimeHigh = Price(null),
            allTimeHighDate = "",
            listedDate = ""
        )

        // Act
        val coinDetails = coinDetailsMapper.mapApiModelToModel(
            apiModel = coinDetailsApiModel,
            currency = currency
        )

        // Assert
        assertThat(coinDetails).isEqualTo(expectedCoinDetails)
    }

    @Test
    fun `When all numbers to format are invalid should return empty values`() {
        // Arrange
        val coinDetailsApiModel = CoinDetailsApiModel(
            coinDetailsDataHolder = CoinDetailsDataHolder(
                coinDetailsData = CoinDetailsData(
                    id = "Qwsogvtv82FCd",
                    name = "Bitcoin",
                    symbol = "BTC",
                    imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                    currentPrice = "29490.954785191607",
                    marketCap = "515076089546.27606",
                    marketCapRank = "1",
                    volume24h = "abcdefg",
                    supply = Supply(
                        circulatingSupply = "abcdefg"
                    ),
                    allTimeHigh = AllTimeHigh(
                        price = "68763.41083248306",
                        timestamp = 1636502400
                    ),
                    listedAt = 1330214400
                )
            )
        )

        val expectedCoinDetails = CoinDetails(
            id = "Qwsogvtv82FCd",
            name = "Bitcoin",
            symbol = "BTC",
            imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
            currentPrice = Price("29490.954785191607"),
            marketCap = Price("515076089546.27606"),
            marketCapRank = "1",
            volume24h = "",
            circulatingSupply = "",
            allTimeHigh = Price("68763.41083248306"),
            allTimeHighDate = "10 Nov 2021",
            listedDate = "26 Feb 2012"
        )

        // Act
        val coinDetails = coinDetailsMapper.mapApiModelToModel(
            apiModel = coinDetailsApiModel,
            currency = currency
        )

        // Assert
        assertThat(coinDetails).isEqualTo(expectedCoinDetails)
    }

    @Test
    fun `When timestamps are invalid should return default values`() {
        // Arrange
        val coinDetailsApiModel = CoinDetailsApiModel(
            coinDetailsDataHolder = CoinDetailsDataHolder(
                coinDetailsData = CoinDetailsData(
                    id = "Qwsogvtv82FCd",
                    name = "Bitcoin",
                    symbol = "BTC",
                    imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                    currentPrice = "29490.954785191607",
                    marketCap = "515076089546.27606",
                    marketCapRank = "1",
                    volume24h = "9294621082.273935",
                    supply = Supply(
                        circulatingSupply = "19508368"
                    ),
                    allTimeHigh = AllTimeHigh(
                        price = "68763.41083248306",
                        timestamp = -1636502400
                    ),
                    listedAt = null
                )
            )
        )

        val expectedCoinDetails = CoinDetails(
            id = "Qwsogvtv82FCd",
            name = "Bitcoin",
            symbol = "BTC",
            imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
            currentPrice = Price("29490.954785191607"),
            marketCap = Price("515076089546.27606"),
            marketCapRank = "1",
            volume24h = "9,294,621,082.274",
            circulatingSupply = "19,508,368",
            allTimeHigh = Price("68763.41083248306"),
            allTimeHighDate = "",
            listedDate = ""
        )

        // Act
        val coinDetails = coinDetailsMapper.mapApiModelToModel(
            apiModel = coinDetailsApiModel,
            currency = currency
        )

        // Assert
        assertThat(coinDetails).isEqualTo(expectedCoinDetails)
    }

    @Test
    fun `When coin details data has valid values should map as expected`() {
        // Arrange
        val coinDetailsApiModel = CoinDetailsApiModel(
            coinDetailsDataHolder = CoinDetailsDataHolder(
                coinDetailsData = CoinDetailsData(
                    id = "Qwsogvtv82FCd",
                    name = "Bitcoin",
                    symbol = "BTC",
                    imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                    currentPrice = "29490.954785191607",
                    marketCap = "515076089546.27606",
                    marketCapRank = "1",
                    volume24h = "9294621082.273935",
                    supply = Supply(
                        circulatingSupply = "19508368"
                    ),
                    allTimeHigh = AllTimeHigh(
                        price = "68763.41083248306",
                        timestamp = 1636502400
                    ),
                    listedAt = 1330214400
                )
            )
        )

        val expectedCoinDetails = CoinDetails(
            id = "Qwsogvtv82FCd",
            name = "Bitcoin",
            symbol = "BTC",
            imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
            currentPrice = Price("29490.954785191607"),
            marketCap = Price("515076089546.27606"),
            marketCapRank = "1",
            volume24h = "9,294,621,082.274",
            circulatingSupply = "19,508,368",
            allTimeHigh = Price("68763.41083248306"),
            allTimeHighDate = "10 Nov 2021",
            listedDate = "26 Feb 2012"
        )

        // Act
        val coinDetails = coinDetailsMapper.mapApiModelToModel(
            coinDetailsApiModel,
            currency = currency
        )

        // Assert
        assertThat(coinDetails).isEqualTo(expectedCoinDetails)
    }
}
