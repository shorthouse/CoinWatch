package dev.shorthouse.coinwatch.data.mapper

import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.data.source.local.preferences.global.Currency
import dev.shorthouse.coinwatch.data.source.remote.model.AllTimeHigh
import dev.shorthouse.coinwatch.data.source.remote.model.CoinDetailsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.CoinDetailsData
import dev.shorthouse.coinwatch.data.source.remote.model.CoinDetailsDataHolder
import dev.shorthouse.coinwatch.data.source.remote.model.CoinDetailsLink
import dev.shorthouse.coinwatch.data.source.remote.model.Supply
import dev.shorthouse.coinwatch.model.CoinDetails
import dev.shorthouse.coinwatch.model.CoinLink
import dev.shorthouse.coinwatch.model.CoinLinkType
import dev.shorthouse.coinwatch.model.Price
import kotlinx.collections.immutable.persistentListOf
import org.junit.Test

class CoinDetailsMapperTest {

    // Class under test
    private val coinDetailsMapper = CoinDetailsMapper()

    private val defaultCurrency = Currency.USD

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
            description = "",
            tags = persistentListOf(),
            links = persistentListOf(),
            imageUrl = "",
            currentPrice = Price(null),
            marketCap = Price(null),
            fullyDilutedMarketCap = Price(null),
            marketCapRank = "",
            volume24h = Price(null),
            numberOfExchanges = "",
            numberOfMarkets = "",
            circulatingSupply = "",
            totalSupply = "",
            maxSupply = "",
            allTimeHigh = Price(null),
            allTimeHighDate = "",
            listedDate = ""
        )

        // Act
        val coinDetails = coinDetailsMapper.mapApiModelToModel(
            apiModel = coinDetailsApiModel,
            currency = defaultCurrency
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
            description = "",
            tags = persistentListOf(),
            links = persistentListOf(),
            imageUrl = "",
            currentPrice = Price(null),
            marketCap = Price(null),
            fullyDilutedMarketCap = Price(null),
            marketCapRank = "",
            volume24h = Price(null),
            numberOfExchanges = "",
            numberOfMarkets = "",
            circulatingSupply = "",
            totalSupply = "",
            maxSupply = "",
            allTimeHigh = Price(null),
            allTimeHighDate = "",
            listedDate = ""
        )

        // Act
        val coinDetails = coinDetailsMapper.mapApiModelToModel(
            apiModel = coinDetailsApiModel,
            currency = defaultCurrency
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
                    description = null,
                    websiteUrl = null,
                    imageUrl = null,
                    currentPrice = null,
                    marketCap = null,
                    fullyDilutedMarketCap = null,
                    marketCapRank = null,
                    volume24h = null,
                    numberOfMarkets = null,
                    numberOfExchanges = null,
                    supply = null,
                    allTimeHigh = null,
                    tags = null,
                    links = null,
                    coinrankingUrl = null,
                    listedAt = null
                )
            )
        )

        val expectedCoinDetails = CoinDetails(
            id = "",
            name = "",
            symbol = "",
            description = "",
            tags = persistentListOf(),
            links = persistentListOf(),
            imageUrl = "",
            currentPrice = Price(null),
            marketCap = Price(null),
            fullyDilutedMarketCap = Price(null),
            marketCapRank = "",
            volume24h = Price(null),
            numberOfExchanges = "",
            numberOfMarkets = "",
            circulatingSupply = "",
            totalSupply = "",
            maxSupply = "",
            allTimeHigh = Price(null),
            allTimeHighDate = "",
            listedDate = ""
        )

        // Act
        val coinDetails = coinDetailsMapper.mapApiModelToModel(
            apiModel = coinDetailsApiModel,
            currency = defaultCurrency
        )

        // Assert
        assertThat(coinDetails).isEqualTo(expectedCoinDetails)
    }

    @Test
    fun `When link values are blank invalid unknown or duplicate should map expected links`() {
        // Arrange
        val coinDetailsApiModel = CoinDetailsApiModel(
            coinDetailsDataHolder = CoinDetailsDataHolder(
                coinDetailsData = CoinDetailsData(
                    id = "Qwsogvtv82FCd",
                    name = "Bitcoin",
                    symbol = "BTC",
                    description = "Bitcoin is the first decentralized digital currency.",
                    websiteUrl = " https://bitcoin.org ",
                    imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                    currentPrice = null,
                    marketCap = null,
                    fullyDilutedMarketCap = null,
                    marketCapRank = null,
                    volume24h = null,
                    numberOfMarkets = null,
                    numberOfExchanges = null,
                    supply = null,
                    allTimeHigh = null,
                    tags = null,
                    links = listOf(
                        CoinDetailsLink(
                            name = "Duplicate website",
                            url = "https://bitcoin.org",
                            type = "website"
                        ),
                        CoinDetailsLink(
                            name = "Blank URL",
                            url = " ",
                            type = "reddit"
                        ),
                        CoinDetailsLink(
                            name = "Invalid URL",
                            url = "not-a-url",
                            type = "reddit"
                        ),
                        CoinDetailsLink(
                            name = "Community Forum",
                            url = "https://bitcointalk.org/",
                            type = "unknown-community"
                        ),
                        CoinDetailsLink(
                            name = " ",
                            url = "https://example.com/bitcoin",
                            type = " "
                        )
                    ),
                    coinrankingUrl = " ",
                    listedAt = null
                )
            )
        )

        val expectedCoinDetails = CoinDetails(
            id = "Qwsogvtv82FCd",
            name = "Bitcoin",
            symbol = "BTC",
            description = "Bitcoin is the first decentralized digital currency.",
            tags = persistentListOf(),
            links = persistentListOf(
                CoinLink(
                    type = CoinLinkType.Website,
                    url = "https://bitcoin.org",
                )
            ),
            imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
            currentPrice = Price(null),
            marketCap = Price(null),
            fullyDilutedMarketCap = Price(null),
            marketCapRank = "",
            volume24h = Price(null),
            numberOfExchanges = "",
            numberOfMarkets = "",
            circulatingSupply = "",
            totalSupply = "",
            maxSupply = "",
            allTimeHigh = Price(null),
            allTimeHighDate = "",
            listedDate = ""
        )

        // Act
        val coinDetails = coinDetailsMapper.mapApiModelToModel(
            apiModel = coinDetailsApiModel,
            currency = defaultCurrency
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
                    description = "Bitcoin is the first decentralized digital currency.",
                    websiteUrl = null,
                    imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                    currentPrice = "29490.954785191607",
                    marketCap = "515076089546.27606",
                    fullyDilutedMarketCap = "abcdefg",
                    marketCapRank = "1",
                    volume24h = "abcdefg",
                    numberOfMarkets = null,
                    numberOfExchanges = null,
                    supply = Supply(
                        circulatingSupply = "abcdefg",
                        totalSupply = "abcdefg",
                        maxSupply = "abcdefg"
                    ),
                    allTimeHigh = AllTimeHigh(
                        price = "68763.41083248306",
                        timestamp = 1636502400
                    ),
                    tags = listOf("store-of-value", "", "  ", "proof-of-work"),
                    links = null,
                    coinrankingUrl = null,
                    listedAt = 1330214400
                )
            )
        )

        val expectedCoinDetails = CoinDetails(
            id = "Qwsogvtv82FCd",
            name = "Bitcoin",
            symbol = "BTC",
            description = "Bitcoin is the first decentralized digital currency.",
            tags = persistentListOf("store-of-value", "proof-of-work"),
            links = persistentListOf(),
            imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
            currentPrice = Price("29490.954785191607"),
            marketCap = Price("515076089546.27606"),
            fullyDilutedMarketCap = Price("abcdefg"),
            marketCapRank = "1",
            volume24h = Price("abcdefg"),
            numberOfExchanges = "",
            numberOfMarkets = "",
            circulatingSupply = "",
            totalSupply = "",
            maxSupply = "",
            allTimeHigh = Price("68763.41083248306"),
            allTimeHighDate = "10 Nov 2021",
            listedDate = "26 Feb 2012"
        )

        // Act
        val coinDetails = coinDetailsMapper.mapApiModelToModel(
            apiModel = coinDetailsApiModel,
            currency = defaultCurrency
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
                    description = "Bitcoin is the first decentralized digital currency.",
                    websiteUrl = null,
                    imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                    currentPrice = "29490.954785191607",
                    marketCap = "515076089546.27606",
                    fullyDilutedMarketCap = "615214637541.4832",
                    marketCapRank = "1",
                    volume24h = "9294621082.273935",
                    numberOfMarkets = 1211,
                    numberOfExchanges = 82,
                    supply = Supply(
                        circulatingSupply = "19508368",
                        totalSupply = "19508368",
                        maxSupply = "21000000"
                    ),
                    allTimeHigh = AllTimeHigh(
                        price = "68763.41083248306",
                        timestamp = -1636502400
                    ),
                    tags = listOf("store-of-value", "proof-of-work"),
                    links = null,
                    coinrankingUrl = null,
                    listedAt = null
                )
            )
        )

        val expectedCoinDetails = CoinDetails(
            id = "Qwsogvtv82FCd",
            name = "Bitcoin",
            symbol = "BTC",
            description = "Bitcoin is the first decentralized digital currency.",
            tags = persistentListOf("store-of-value", "proof-of-work"),
            links = persistentListOf(),
            imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
            currentPrice = Price("29490.954785191607"),
            marketCap = Price("515076089546.27606"),
            fullyDilutedMarketCap = Price("615214637541.4832"),
            marketCapRank = "1",
            volume24h = Price("9294621082.273935"),
            numberOfExchanges = "82",
            numberOfMarkets = "1,211",
            circulatingSupply = "19,508,368",
            totalSupply = "19,508,368",
            maxSupply = "21,000,000",
            allTimeHigh = Price("68763.41083248306"),
            allTimeHighDate = "",
            listedDate = ""
        )

        // Act
        val coinDetails = coinDetailsMapper.mapApiModelToModel(
            apiModel = coinDetailsApiModel,
            currency = defaultCurrency
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
                    description = "Bitcoin is the first decentralized digital currency.",
                    websiteUrl = "https://bitcoin.org",
                    imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                    currentPrice = "29490.954785191607",
                    marketCap = "515076089546.27606",
                    fullyDilutedMarketCap = "615214637541.4832",
                    marketCapRank = "1",
                    volume24h = "9294621082.273935",
                    numberOfMarkets = 1211,
                    numberOfExchanges = 82,
                    supply = Supply(
                        circulatingSupply = "19508368",
                        totalSupply = "19508368",
                        maxSupply = "21000000"
                    ),
                    allTimeHigh = AllTimeHigh(
                        price = "68763.41083248306",
                        timestamp = 1636502400
                    ),
                    tags = listOf("store-of-value", "", "proof-of-work"),
                    links = listOf(
                        CoinDetailsLink(
                            name = "Bitcoin",
                            url = "https://bitcoin.org",
                            type = "website"
                        ),
                        CoinDetailsLink(
                            name = "Bitcoin subreddit",
                            url = "https://www.reddit.com/r/Bitcoin/",
                            type = "reddit"
                        ),
                        CoinDetailsLink(
                            name = "Bitcoin Core",
                            url = "https://github.com/bitcoin/bitcoin",
                            type = "github"
                        ),
                        CoinDetailsLink(
                            name = "@bitcoin",
                            url = "https://twitter.com/bitcoin",
                            type = "twitter"
                        ),
                        CoinDetailsLink(
                            name = "Bitcoin YouTube",
                            url = "https://www.youtube.com/@Bitcoin",
                            type = "youtube"
                        )
                    ),
                    coinrankingUrl = "https://coinranking.com/coin/Qwsogvtv82FCd+bitcoin-btc",
                    listedAt = 1330214400
                )
            )
        )

        val expectedCoinDetails = CoinDetails(
            id = "Qwsogvtv82FCd",
            name = "Bitcoin",
            symbol = "BTC",
            description = "Bitcoin is the first decentralized digital currency.",
            tags = persistentListOf("store-of-value", "proof-of-work"),
            links = persistentListOf(
                CoinLink(
                    type = CoinLinkType.Website,
                    url = "https://bitcoin.org",
                ),
                CoinLink(
                    type = CoinLinkType.Reddit,
                    url = "https://www.reddit.com/r/Bitcoin/",
                ),
                CoinLink(
                    type = CoinLinkType.GitHub,
                    url = "https://github.com/bitcoin/bitcoin",
                ),
                CoinLink(
                    type = CoinLinkType.X,
                    url = "https://twitter.com/bitcoin",
                ),
                CoinLink(
                    type = CoinLinkType.YouTube,
                    url = "https://www.youtube.com/@Bitcoin",
                )
            ),
            imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
            currentPrice = Price("29490.954785191607"),
            marketCap = Price("515076089546.27606"),
            fullyDilutedMarketCap = Price("615214637541.4832"),
            marketCapRank = "1",
            volume24h = Price("9294621082.273935"),
            numberOfExchanges = "82",
            numberOfMarkets = "1,211",
            circulatingSupply = "19,508,368",
            totalSupply = "19,508,368",
            maxSupply = "21,000,000",
            allTimeHigh = Price("68763.41083248306"),
            allTimeHighDate = "10 Nov 2021",
            listedDate = "26 Feb 2012"
        )

        // Act
        val coinDetails = coinDetailsMapper.mapApiModelToModel(
            coinDetailsApiModel,
            currency = defaultCurrency
        )

        // Assert
        assertThat(coinDetails).isEqualTo(expectedCoinDetails)
    }

    @Test
    fun `When coin details has gbp currency with valid values should map expected coin details`() {
        // Arrange
        val currency = Currency.GBP

        val coinDetailsApiModel = CoinDetailsApiModel(
            coinDetailsDataHolder = CoinDetailsDataHolder(
                coinDetailsData = CoinDetailsData(
                    id = "Qwsogvtv82FCd",
                    name = "Bitcoin",
                    symbol = "BTC",
                    description = "Bitcoin is the first decentralized digital currency.",
                    websiteUrl = "https://bitcoin.org",
                    imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                    currentPrice = "29490.954785191607",
                    marketCap = "515076089546.27606",
                    fullyDilutedMarketCap = "615214637541.4832",
                    marketCapRank = "1",
                    volume24h = "9294621082.273935",
                    numberOfMarkets = 1211,
                    numberOfExchanges = 82,
                    supply = Supply(
                        circulatingSupply = "19508368",
                        totalSupply = "19508368",
                        maxSupply = "21000000"
                    ),
                    allTimeHigh = AllTimeHigh(
                        price = "68763.41083248306",
                        timestamp = 1636502400
                    ),
                    tags = listOf("store-of-value", "proof-of-work"),
                    links = listOf(
                        CoinDetailsLink(
                            name = "Bitcoin subreddit",
                            url = "https://www.reddit.com/r/Bitcoin/",
                            type = "reddit"
                        )
                    ),
                    coinrankingUrl = "https://coinranking.com/coin/Qwsogvtv82FCd+bitcoin-btc",
                    listedAt = 1330214400
                )
            )
        )

        val expectedCoinDetails = CoinDetails(
            id = "Qwsogvtv82FCd",
            name = "Bitcoin",
            symbol = "BTC",
            description = "Bitcoin is the first decentralized digital currency.",
            tags = persistentListOf("store-of-value", "proof-of-work"),
            links = persistentListOf(
                CoinLink(
                    type = CoinLinkType.Website,
                    url = "https://bitcoin.org",
                ),
                CoinLink(
                    type = CoinLinkType.Reddit,
                    url = "https://www.reddit.com/r/Bitcoin/",
                )
            ),
            imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
            currentPrice = Price("29490.954785191607", currency = currency),
            marketCap = Price("515076089546.27606", currency = currency),
            fullyDilutedMarketCap = Price("615214637541.4832", currency = currency),
            marketCapRank = "1",
            volume24h = Price("9294621082.273935", currency = currency),
            numberOfExchanges = "82",
            numberOfMarkets = "1,211",
            circulatingSupply = "19,508,368",
            totalSupply = "19,508,368",
            maxSupply = "21,000,000",
            allTimeHigh = Price("68763.41083248306", currency = currency),
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

    @Test
    fun `When coin details has eur currency with valid values should map expected coin details`() {
        // Arrange
        val currency = Currency.EUR

        val coinDetailsApiModel = CoinDetailsApiModel(
            coinDetailsDataHolder = CoinDetailsDataHolder(
                coinDetailsData = CoinDetailsData(
                    id = "Qwsogvtv82FCd",
                    name = "Bitcoin",
                    symbol = "BTC",
                    description = "Bitcoin is the first decentralized digital currency.",
                    websiteUrl = "https://bitcoin.org",
                    imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                    currentPrice = "29490.954785191607",
                    marketCap = "515076089546.27606",
                    fullyDilutedMarketCap = "615214637541.4832",
                    marketCapRank = "1",
                    volume24h = "9294621082.273935",
                    numberOfMarkets = 1211,
                    numberOfExchanges = 82,
                    supply = Supply(
                        circulatingSupply = "19508368",
                        totalSupply = "19508368",
                        maxSupply = "21000000"
                    ),
                    allTimeHigh = AllTimeHigh(
                        price = "68763.41083248306",
                        timestamp = 1636502400
                    ),
                    tags = listOf("store-of-value", "proof-of-work"),
                    links = listOf(
                        CoinDetailsLink(
                            name = "Bitcoin subreddit",
                            url = "https://www.reddit.com/r/Bitcoin/",
                            type = "reddit"
                        )
                    ),
                    coinrankingUrl = "https://coinranking.com/coin/Qwsogvtv82FCd+bitcoin-btc",
                    listedAt = 1330214400
                )
            )
        )

        val expectedCoinDetails = CoinDetails(
            id = "Qwsogvtv82FCd",
            name = "Bitcoin",
            symbol = "BTC",
            description = "Bitcoin is the first decentralized digital currency.",
            tags = persistentListOf("store-of-value", "proof-of-work"),
            links = persistentListOf(
                CoinLink(
                    type = CoinLinkType.Website,
                    url = "https://bitcoin.org",
                ),
                CoinLink(
                    type = CoinLinkType.Reddit,
                    url = "https://www.reddit.com/r/Bitcoin/",
                )
            ),
            imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
            currentPrice = Price("29490.954785191607", currency = currency),
            marketCap = Price("515076089546.27606", currency = currency),
            fullyDilutedMarketCap = Price("615214637541.4832", currency = currency),
            marketCapRank = "1",
            volume24h = Price("9294621082.273935", currency = currency),
            numberOfExchanges = "82",
            numberOfMarkets = "1,211",
            circulatingSupply = "19,508,368",
            totalSupply = "19,508,368",
            maxSupply = "21,000,000",
            allTimeHigh = Price("68763.41083248306", currency = currency),
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
