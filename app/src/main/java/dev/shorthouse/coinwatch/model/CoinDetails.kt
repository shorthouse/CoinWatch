package dev.shorthouse.coinwatch.model

import kotlinx.collections.immutable.ImmutableList

data class CoinDetails(
    val id: String,
    val name: String,
    val symbol: String,
    val description: String,
    val tags: ImmutableList<String>,
    val links: ImmutableList<CoinLink>,
    val imageUrl: String,
    val currentPrice: Price,
    val marketCap: Price,
    val fullyDilutedMarketCap: Price,
    val marketCapRank: String,
    val volume24h: Price,
    val numberOfExchanges: String,
    val numberOfMarkets: String,
    val circulatingSupply: String,
    val totalSupply: String,
    val maxSupply: String,
    val allTimeHigh: Price,
    val allTimeHighDate: String,
    val listedDate: String
)
