package dev.shorthouse.cryptodata.model

data class CoinDetail(
    val id: String,
    val symbol: String,
    val name: String,
    val image: String,
    val currentPrice: Double,
    val priceChangePercentage: Double,
    val genesisDate: String,
//    val allTimeHigh: String,
//    val allTimeHighDate: String,
//    val allTimeLow: String,
//    val allTimeLowDate: String,
//    val marketCap: Long,
//    val marketCapChangePercentage: String,
//    val marketCapRank: Int,
    val description: String,
    val homepageLink: String?,
    val blockchainLink: String?,
    val subredditLink: String?
)
