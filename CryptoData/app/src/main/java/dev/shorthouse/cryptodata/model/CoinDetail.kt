package dev.shorthouse.cryptodata.model

import dev.shorthouse.cryptodata.data.source.remote.dto.CoinDetailApiModel
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

data class CoinDetail(
    val id: String,
    val name: String,
    val symbol: String,
    val image: String,
    val currentPrice: String,
    val priceChangePercentage24h: Double,
    val historicalPrices7d: List<Double>,
    val marketCapRank: Int,
    val marketCap: String,
    val totalSupply: Double,
    val allTimeLow: String,
    val allTimeHigh: String,
    val allTimeLowDate: String,
    val allTimeHighDate: String,
    val genesisDate: String
)

fun CoinDetailApiModel.toCoinDetail(): CoinDetail {
    val dateFormatter = DateTimeFormatter.ofPattern("d MM yyyy", Locale.getDefault())
//
    val allTimeLowLocalDate = LocalDateTime.parse(marketData.allTimeLowDate.usd)
//    val allTimeHighLocalDate = LocalDateTime.parse(marketData.allTimeHighDate.usd)
//    val genesisLocalDate = LocalDateTime.parse(
//        genesisDate,
//        DateTimeFormatter.ofPattern("yyyy-mm-dd")
//    )

    val currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US)

    return CoinDetail(
        id = id,
        name = name,
        symbol = symbol.uppercase(),
        image = image.large,
        currentPrice = currencyFormatter.format(marketData.currentPrice.usd),
        priceChangePercentage24h = marketData.priceChangePercentage24h,
        historicalPrices7d = marketData.historicalPrices7d.usd,
        marketCapRank = marketData.marketCapRank,
        marketCap = currencyFormatter.format(marketData.marketCap.usd),
        totalSupply = marketData.totalSupply,
        allTimeLow = currencyFormatter.format(marketData.allTimeLow.usd),
        allTimeHigh = currencyFormatter.format(marketData.allTimeHigh.usd),
        allTimeLowDate = "formatDate(marketData.allTimeLowDate.usd)",
        allTimeHighDate = "allTimeHighLocalDate.format(dateFormatter)",
        genesisDate = "genesisLocalDate.format(dateFormatter)"
    )
}

private fun formatDate(dateTimeString: String): String {
    val formatter = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.getDefault())
    val dateTime = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ISO_DATE_TIME)
    return dateTime.format(formatter)
}
