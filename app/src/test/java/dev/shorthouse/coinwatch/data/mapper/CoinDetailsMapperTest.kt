package dev.shorthouse.coinwatch.data.mapper

import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.common.Constants.MISSING_VALUE_PLACEHOLDER
import dev.shorthouse.coinwatch.data.source.local.datastore.global.Currency
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
import dev.shorthouse.coinwatch.rule.LocaleRule
import kotlinx.collections.immutable.persistentListOf
import org.junit.Rule
import org.junit.Test
import java.util.Locale

class CoinDetailsMapperTest {

    @get:Rule
    val localeRule = LocaleRule(Locale.US)

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
            marketCapRank = MISSING_VALUE_PLACEHOLDER,
            volume24h = Price(null),
            numberOfExchanges = MISSING_VALUE_PLACEHOLDER,
            numberOfMarkets = MISSING_VALUE_PLACEHOLDER,
            circulatingSupply = MISSING_VALUE_PLACEHOLDER,
            totalSupply = MISSING_VALUE_PLACEHOLDER,
            maxSupply = MISSING_VALUE_PLACEHOLDER,
            allTimeHigh = Price(null),
            allTimeHighDate = MISSING_VALUE_PLACEHOLDER,
            listedDate = MISSING_VALUE_PLACEHOLDER
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
            marketCapRank = MISSING_VALUE_PLACEHOLDER,
            volume24h = Price(null),
            numberOfExchanges = MISSING_VALUE_PLACEHOLDER,
            numberOfMarkets = MISSING_VALUE_PLACEHOLDER,
            circulatingSupply = MISSING_VALUE_PLACEHOLDER,
            totalSupply = MISSING_VALUE_PLACEHOLDER,
            maxSupply = MISSING_VALUE_PLACEHOLDER,
            allTimeHigh = Price(null),
            allTimeHighDate = MISSING_VALUE_PLACEHOLDER,
            listedDate = MISSING_VALUE_PLACEHOLDER
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
            marketCapRank = MISSING_VALUE_PLACEHOLDER,
            volume24h = Price(null),
            numberOfExchanges = MISSING_VALUE_PLACEHOLDER,
            numberOfMarkets = MISSING_VALUE_PLACEHOLDER,
            circulatingSupply = MISSING_VALUE_PLACEHOLDER,
            totalSupply = MISSING_VALUE_PLACEHOLDER,
            maxSupply = MISSING_VALUE_PLACEHOLDER,
            allTimeHigh = Price(null),
            allTimeHighDate = MISSING_VALUE_PLACEHOLDER,
            listedDate = MISSING_VALUE_PLACEHOLDER
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
    fun `When market cap rank is blank should return unavailable value`() {
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
                    marketCapRank = "  ",
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

        // Act
        val coinDetails = coinDetailsMapper.mapApiModelToModel(
            apiModel = coinDetailsApiModel,
            currency = defaultCurrency
        )

        // Assert
        assertThat(coinDetails.marketCapRank).isEqualTo(MISSING_VALUE_PLACEHOLDER)
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
            marketCapRank = MISSING_VALUE_PLACEHOLDER,
            volume24h = Price(null),
            numberOfExchanges = MISSING_VALUE_PLACEHOLDER,
            numberOfMarkets = MISSING_VALUE_PLACEHOLDER,
            circulatingSupply = MISSING_VALUE_PLACEHOLDER,
            totalSupply = MISSING_VALUE_PLACEHOLDER,
            maxSupply = MISSING_VALUE_PLACEHOLDER,
            allTimeHigh = Price(null),
            allTimeHighDate = MISSING_VALUE_PLACEHOLDER,
            listedDate = MISSING_VALUE_PLACEHOLDER
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
    fun `When links use unsupported schemes should only map http links`() {
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
                    links = listOf(
                        CoinDetailsLink(
                            name = "FTP",
                            url = "ftp://example.com",
                            type = "reddit"
                        ),
                        CoinDetailsLink(
                            name = "Email",
                            url = "mailto:test@example.com",
                            type = "github"
                        ),
                        CoinDetailsLink(
                            name = "Reddit",
                            url = "https://www.reddit.com/r/Bitcoin/",
                            type = "reddit"
                        )
                    ),
                    coinrankingUrl = null,
                    listedAt = null
                )
            )
        )

        // Act
        val coinDetails = coinDetailsMapper.mapApiModelToModel(
            apiModel = coinDetailsApiModel,
            currency = defaultCurrency
        )

        // Assert
        assertThat(coinDetails.links).containsExactly(
            CoinLink(
                type = CoinLinkType.Reddit,
                url = "https://www.reddit.com/r/Bitcoin/"
            )
        )
    }

    @Test
    fun `When numbers to format are zero should return zero values`() {
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
                    numberOfMarkets = 0,
                    numberOfExchanges = 0,
                    supply = Supply(
                        circulatingSupply = "0",
                        totalSupply = "0",
                        maxSupply = "0"
                    ),
                    allTimeHigh = null,
                    tags = null,
                    links = null,
                    coinrankingUrl = null,
                    listedAt = null
                )
            )
        )

        // Act
        val coinDetails = coinDetailsMapper.mapApiModelToModel(
            apiModel = coinDetailsApiModel,
            currency = defaultCurrency
        )

        // Assert
        assertThat(coinDetails.numberOfExchanges).isEqualTo("0")
        assertThat(coinDetails.numberOfMarkets).isEqualTo("0")
        assertThat(coinDetails.circulatingSupply).isEqualTo("0")
        assertThat(coinDetails.totalSupply).isEqualTo("0")
        assertThat(coinDetails.maxSupply).isEqualTo("0")
    }

    @Test
    fun `When all numbers to format are invalid should return unavailable values`() {
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
            numberOfExchanges = MISSING_VALUE_PLACEHOLDER,
            numberOfMarkets = MISSING_VALUE_PLACEHOLDER,
            circulatingSupply = MISSING_VALUE_PLACEHOLDER,
            totalSupply = MISSING_VALUE_PLACEHOLDER,
            maxSupply = MISSING_VALUE_PLACEHOLDER,
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
    fun `When supply values are blank should return unavailable values`() {
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
                    supply = Supply(
                        circulatingSupply = "",
                        totalSupply = " ",
                        maxSupply = "\t"
                    ),
                    allTimeHigh = null,
                    tags = null,
                    links = null,
                    coinrankingUrl = null,
                    listedAt = null
                )
            )
        )

        // Act
        val coinDetails = coinDetailsMapper.mapApiModelToModel(
            apiModel = coinDetailsApiModel,
            currency = defaultCurrency
        )

        // Assert
        assertThat(coinDetails.circulatingSupply).isEqualTo(MISSING_VALUE_PLACEHOLDER)
        assertThat(coinDetails.totalSupply).isEqualTo(MISSING_VALUE_PLACEHOLDER)
        assertThat(coinDetails.maxSupply).isEqualTo(MISSING_VALUE_PLACEHOLDER)
    }

    @Test
    fun `When format locale is passed should format grouped numbers with expected separators`() {
        // Arrange
        val coinDetailsApiModel = validNumbersCoinDetailsApiModel()

        val cases = listOf(
            Locale.US to ExpectedNumbers(
                numberOfExchanges = "82",
                numberOfMarkets = "1,211",
                circulatingSupply = "19,508,368",
                totalSupply = "19,508,368",
                maxSupply = "21,000,000"
            ),
            Locale.GERMANY to ExpectedNumbers(
                numberOfExchanges = "82",
                numberOfMarkets = "1.211",
                circulatingSupply = "19.508.368",
                totalSupply = "19.508.368",
                maxSupply = "21.000.000"
            ),
            Locale.FRANCE to ExpectedNumbers(
                numberOfExchanges = "82",
                numberOfMarkets = "1\u202F211",
                circulatingSupply = "19\u202F508\u202F368",
                totalSupply = "19\u202F508\u202F368",
                maxSupply = "21\u202F000\u202F000"
            ),
            Locale.forLanguageTag("ar-SA") to ExpectedNumbers(
                numberOfExchanges = "\u0668\u0662",
                numberOfMarkets = "\u0661\u066C\u0662\u0661\u0661",
                circulatingSupply = "\u0661\u0669\u066C\u0665\u0660\u0668\u066C\u0663\u0666\u0668",
                totalSupply = "\u0661\u0669\u066C\u0665\u0660\u0668\u066C\u0663\u0666\u0668",
                maxSupply = "\u0662\u0661\u066C\u0660\u0660\u0660\u066C\u0660\u0660\u0660"
            )
        )

        cases.forEach { (locale, expectedNumbers) ->
            // Act
            val coinDetails = coinDetailsMapper.mapApiModelToModel(
                apiModel = coinDetailsApiModel,
                currency = defaultCurrency,
                formatLocale = locale
            )

            // Assert
            assertThat(coinDetails.numberOfExchanges).isEqualTo(expectedNumbers.numberOfExchanges)
            assertThat(coinDetails.numberOfMarkets).isEqualTo(expectedNumbers.numberOfMarkets)
            assertThat(coinDetails.circulatingSupply).isEqualTo(expectedNumbers.circulatingSupply)
            assertThat(coinDetails.totalSupply).isEqualTo(expectedNumbers.totalSupply)
            assertThat(coinDetails.maxSupply).isEqualTo(expectedNumbers.maxSupply)
        }
    }

    @Test
    fun `When format locale default is changed should format grouped numbers with format locale`() {
        localeRule.withLocales(displayLocale = Locale.US, formatLocale = Locale.GERMANY) {
            // Arrange
            val coinDetailsApiModel = validNumbersCoinDetailsApiModel()

            // Act
            val coinDetails = coinDetailsMapper.mapApiModelToModel(
                apiModel = coinDetailsApiModel,
                currency = defaultCurrency
            )

            // Assert
            assertThat(coinDetails.numberOfExchanges).isEqualTo("82")
            assertThat(coinDetails.numberOfMarkets).isEqualTo("1.211")
            assertThat(coinDetails.circulatingSupply).isEqualTo("19.508.368")
            assertThat(coinDetails.totalSupply).isEqualTo("19.508.368")
            assertThat(coinDetails.maxSupply).isEqualTo("21.000.000")
        }
    }

    @Test
    fun `When timestamps are invalid should return unavailable values`() {
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
            allTimeHighDate = MISSING_VALUE_PLACEHOLDER,
            listedDate = MISSING_VALUE_PLACEHOLDER
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
    fun `When timestamps are out of range should return unavailable values`() {
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
                    allTimeHigh = AllTimeHigh(
                        price = null,
                        timestamp = Long.MAX_VALUE
                    ),
                    tags = null,
                    links = null,
                    coinrankingUrl = null,
                    listedAt = Long.MAX_VALUE
                )
            )
        )

        // Act
        val coinDetails = coinDetailsMapper.mapApiModelToModel(
            apiModel = coinDetailsApiModel,
            currency = defaultCurrency
        )

        // Assert
        assertThat(coinDetails.allTimeHighDate).isEqualTo(MISSING_VALUE_PLACEHOLDER)
        assertThat(coinDetails.listedDate).isEqualTo(MISSING_VALUE_PLACEHOLDER)
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

    private fun validNumbersCoinDetailsApiModel() = CoinDetailsApiModel(
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
                numberOfMarkets = 1211,
                numberOfExchanges = 82,
                supply = Supply(
                    circulatingSupply = "19508368",
                    totalSupply = "19508368",
                    maxSupply = "21000000"
                ),
                allTimeHigh = null,
                tags = null,
                links = null,
                coinrankingUrl = null,
                listedAt = null
            )
        )
    )

    private data class ExpectedNumbers(
        val numberOfExchanges: String,
        val numberOfMarkets: String,
        val circulatingSupply: String,
        val totalSupply: String,
        val maxSupply: String,
    )
}
