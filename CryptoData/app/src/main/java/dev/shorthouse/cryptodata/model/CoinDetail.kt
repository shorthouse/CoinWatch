package dev.shorthouse.cryptodata.model

import dev.shorthouse.cryptodata.data.source.remote.dto.CoinDetailDto

data class CoinDetail(
    val id: String,
    val symbol: String,
    val name: String,
    val image: String,
    val currentPrice: Double,
    val priceChangePercentage: Double,

    val historicalPrices: List<Double>,

    val marketCap: Long,
    val marketCapChangePercentage: Double,
    val marketCapRank: Int,
    val dailyHigh: Double,
    val dailyHighChangePercentage: Double,
    val dailyLow: Double,
    val dailyLowChangePercentage: Double,

    val genesisDate: String,
    val allTimeHigh: Double,
    val allTimeHighDate: String,
    val allTimeLow: Double,
    val allTimeLowDate: String,

    val description: String,

    val homepageLink: String?,
    val githubLink: String?,
    val subredditLink: String?
)

fun CoinDetailDto.toCoinDetail(): CoinDetail {
    val currentPrice = marketData.currentPrice.usd
    val dailyHigh = marketData.dailyHigh.usd
    val dailyLow = marketData.dailyLow.usd

    val dailyHighChangePercentage = 100 * (1 - (currentPrice / dailyHigh))
    val dailyLowChangePercentage = 100 * (currentPrice / dailyLow)

    return CoinDetail(
        id = id,
        symbol = symbol.uppercase(),
        name = name,
        image = image.large,
        currentPrice = currentPrice,
        priceChangePercentage = marketData.priceChange,
        genesisDate = genesisDate,
        allTimeHigh = marketData.allTimeHigh.usd,
        allTimeHighDate = marketData.allTimeHighDate.usd,
        allTimeLow = marketData.allTimeLow.usd,
        allTimeLowDate = marketData.allTimeLowDate.usd,
        marketCapRank = marketData.marketCapRank,
        marketCap = marketData.marketCap.usd,
        marketCapChangePercentage = marketData.marketCapChangePercentage,
        dailyHigh = dailyHigh,
        dailyHighChangePercentage = dailyHighChangePercentage,
        dailyLow = dailyLow,
        dailyLowChangePercentage = dailyLowChangePercentage,
        historicalPrices = marketData.historicalPrices.usd,
        description = description.en,
        homepageLink = links.homepage.firstOrNull(),
        githubLink = links.reposUrl.github.firstOrNull(),
        subredditLink = links.subreddit
    )
}
