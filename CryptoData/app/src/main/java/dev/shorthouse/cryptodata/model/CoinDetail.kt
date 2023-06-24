package dev.shorthouse.cryptodata.model

import dev.shorthouse.cryptodata.data.source.remote.dto.CoinDetailDto

data class CoinDetail(
    val id: String,
    val name: String,
    val symbol: String,
    val image: String,
    val currentPrice: Double,
    val priceChangePercentage24h: Double,
    val historicalPrices7d: List<Double>,
    val dailyHigh: Double,
    val dailyHighChangePercentage: Double,
    val dailyLow: Double,
    val dailyLowChangePercentage: Double,
    val marketCapRank: Int,
    val marketCap: Long,
    val totalSupply: Double,
    val allTimeLow: Double,
    val allTimeHigh: Double,
    val allTimeLowDate: String,
    val allTimeHighDate: String,
    val genesisDate: String,
)

fun CoinDetailDto.toCoinDetail(): CoinDetail {
    val currentPrice = marketData.currentPrice.usd
    val dailyHigh = marketData.dailyHigh.usd
    val dailyLow = marketData.dailyLow.usd

    val dailyHighChangePercentage = 100 * (1 - (currentPrice / dailyHigh))
    val dailyLowChangePercentage = 100 * (currentPrice / dailyLow)

    return CoinDetail(
        id = id,
        name = name,
        symbol = symbol.uppercase(),
        image = image.large,
        currentPrice = currentPrice,
        priceChangePercentage24h = marketData.priceChangePercentage24h,
        historicalPrices7d = marketData.historicalPrices7d.usd,
        dailyHigh = dailyHigh,
        dailyHighChangePercentage = dailyHighChangePercentage,
        dailyLow = dailyLow,
        dailyLowChangePercentage = dailyLowChangePercentage,
        marketCapRank = marketData.marketCapRank,
        marketCap = marketData.marketCap.usd,
        totalSupply = marketData.totalSupply,
        allTimeLow = marketData.allTimeLow.usd,
        allTimeHigh = marketData.allTimeHigh.usd,
        allTimeLowDate = marketData.allTimeLowDate.usd,
        allTimeHighDate = marketData.allTimeHighDate.usd,
        genesisDate = genesisDate,
    )
}
