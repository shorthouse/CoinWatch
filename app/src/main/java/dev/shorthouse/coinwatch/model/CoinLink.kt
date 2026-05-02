package dev.shorthouse.coinwatch.model

data class CoinLink(
    val type: CoinLinkType,
    val url: String,
)

enum class CoinLinkType {
    Website,
    Whitepaper,
    Discord,
    GitHub,
    Reddit,
    Telegram,
    X,
    YouTube
}
