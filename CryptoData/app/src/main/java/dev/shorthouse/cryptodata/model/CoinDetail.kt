package dev.shorthouse.cryptodata.model

data class CoinDetail(
    val id: String,
    val symbol: String,
    val name: String,
    val image: String,
    val currentPrice: Double,
    val priceChangePercentage: Double,

    val marketCap: Long,
    val marketCapChangePercentage: Double,
    val marketCapRank: Int,
    val dailyHigh: Double,
    val dailyHighChangePercentage: Double,
    val dailyLow: Double,
    val dailyLowChangePercentage: Double,

    val genesisDate: String,
    //    val allTimeHigh: String,
    //    val allTimeHighDate: String,
    //    val allTimeLow: String,
    //    val allTimeLowDate: String,

    val description: String,

    val homepageLink: String?,
    val githubLink: String?,
    val subredditLink: String?
)
