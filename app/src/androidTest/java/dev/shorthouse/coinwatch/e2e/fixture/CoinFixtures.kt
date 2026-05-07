package dev.shorthouse.coinwatch.e2e.fixture

import dev.shorthouse.coinwatch.data.source.remote.model.AllTimeHigh
import dev.shorthouse.coinwatch.data.source.remote.model.CoinApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.CoinDetailsData
import dev.shorthouse.coinwatch.data.source.remote.model.CoinDetailsLink
import dev.shorthouse.coinwatch.data.source.remote.model.Supply
import dev.shorthouse.coinwatch.model.Price

object Bitcoin {
    const val ID = "Qwsogvtv82FCd"
    const val NAME = "Bitcoin"
    const val SYMBOL = "BTC"
    const val IMAGE_URL = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg"
    const val RAW_PRICE = "29446.336548759988"
    const val PRICE_CHANGE_PERCENTAGE_24H = "1.76833"

    val FORMATTED_PRICE: String = Price(RAW_PRICE).formattedAmount

    fun coinApiModel() = CoinApiModel(
        id = ID,
        symbol = SYMBOL,
        name = NAME,
        imageUrl = IMAGE_URL,
        currentPrice = RAW_PRICE,
        priceChangePercentage24h = PRICE_CHANGE_PERCENTAGE_24H,
    )

    fun coinDetailsData() = CoinDetailsData(
        id = ID,
        name = NAME,
        symbol = SYMBOL,
        description = "Bitcoin is the first decentralized cryptocurrency.",
        websiteUrl = "https://bitcoin.org",
        imageUrl = IMAGE_URL,
        currentPrice = RAW_PRICE,
        marketCap = "570000000000",
        fullyDilutedMarketCap = "618000000000",
        marketCapRank = "1",
        volume24h = "12000000000",
        numberOfMarkets = 10_000,
        numberOfExchanges = 200,
        supply = Supply(
            circulatingSupply = "19400000",
            totalSupply = "19400000",
            maxSupply = "21000000",
        ),
        allTimeHigh = AllTimeHigh(
            price = "69000",
            timestamp = 1636502400L,
        ),
        tags = listOf("proof-of-work", "store-of-value"),
        links = listOf(
            CoinDetailsLink(
                name = "Website",
                url = "https://bitcoin.org",
                type = "website",
            )
        ),
        coinrankingUrl = "https://coinranking.com/coin/Qwsogvtv82FCd+bitcoin-btc",
        listedAt = 1230940800L,
    )
}

object Ethereum {
    const val ID = "razxDUgYGNAdQ"
    const val NAME = "Ethereum"
    const val SYMBOL = "ETH"
    const val IMAGE_URL = "https://cdn.coinranking.com/rk4RKHOuW/eth.svg"
    const val RAW_PRICE = "1875.473083380222"
    const val PRICE_CHANGE_PERCENTAGE_24H = "-1.84"

    val FORMATTED_PRICE: String = Price(RAW_PRICE).formattedAmount

    fun coinApiModel() = CoinApiModel(
        id = ID,
        symbol = SYMBOL,
        name = NAME,
        imageUrl = IMAGE_URL,
        currentPrice = RAW_PRICE,
        priceChangePercentage24h = PRICE_CHANGE_PERCENTAGE_24H,
    )
}
