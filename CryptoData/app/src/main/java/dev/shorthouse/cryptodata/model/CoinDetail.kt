package dev.shorthouse.cryptodata.model

import dev.shorthouse.cryptodata.data.source.remote.dto.CoinDetailApiModel
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Currency
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
    val circulatingSupply: String,
    val allTimeLow: String,
    val allTimeHigh: String,
    val allTimeLowDate: String,
    val allTimeHighDate: String,
)

fun CoinDetailApiModel.toCoinDetail(): CoinDetail {
    val currencyDecimalFormatter = NumberFormat.getCurrencyInstance(Locale.getDefault()).apply {
        currency = Currency.getInstance("USD")
    }
    val currencyIntegerFormatter = NumberFormat.getCurrencyInstance(Locale.getDefault()).apply {
        currency = Currency.getInstance("USD")
        maximumFractionDigits = 0
    }

    val dateFormatter = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.getDefault())
    val allTimeLowLocalDate = LocalDateTime.parse(
        marketData.allTimeLowDate.usd,
        DateTimeFormatter.ISO_DATE_TIME
    )
    val allTimeHighLocalDate = LocalDateTime.parse(
        marketData.allTimeHighDate.usd,
        DateTimeFormatter.ISO_DATE_TIME
    )

    val decimalFormat = DecimalFormat(
        "#,###",
        DecimalFormatSymbols.getInstance(Locale.getDefault())
    )

    return CoinDetail(
        id = id,
        name = name,
        symbol = symbol.uppercase(),
        image = image.large,
        currentPrice = currencyDecimalFormatter.format(marketData.currentPrice.usd),
        priceChangePercentage24h = marketData.priceChangePercentage24h,
        historicalPrices7d = marketData.historicalPrices7d.usd,
        marketCapRank = marketData.marketCapRank,
        marketCap = currencyIntegerFormatter.format(marketData.marketCap.usd),
        circulatingSupply = decimalFormat.format(marketData.circulatingSupply),
        allTimeLow = currencyDecimalFormatter.format(marketData.allTimeLow.usd),
        allTimeHigh = currencyDecimalFormatter.format(marketData.allTimeHigh.usd),
        allTimeLowDate = dateFormatter.format(allTimeLowLocalDate),
        allTimeHighDate = dateFormatter.format(allTimeHighLocalDate),
    )
}
