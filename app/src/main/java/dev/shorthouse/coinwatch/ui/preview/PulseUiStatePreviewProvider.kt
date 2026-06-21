package dev.shorthouse.coinwatch.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import dev.shorthouse.coinwatch.R
import dev.shorthouse.coinwatch.data.source.local.datastore.global.Currency
import dev.shorthouse.coinwatch.model.FearGreed
import dev.shorthouse.coinwatch.model.FearGreedMoodBand
import dev.shorthouse.coinwatch.model.GlobalMarket
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.Price
import dev.shorthouse.coinwatch.model.TrendingCoin
import dev.shorthouse.coinwatch.ui.preview.PulsePreviewData.fearGreedFear
import dev.shorthouse.coinwatch.ui.preview.PulsePreviewData.fearGreedGreed
import dev.shorthouse.coinwatch.ui.preview.PulsePreviewData.globalMarket
import dev.shorthouse.coinwatch.ui.preview.PulsePreviewData.trendingCoins
import dev.shorthouse.coinwatch.ui.screen.pulse.PulseUiState
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import java.math.BigDecimal

class PulseUiStatePreviewProvider : PreviewParameterProvider<PulseUiState> {
    override val values = sequenceOf(
        PulseUiState(
            fearGreed = fearGreedFear,
            globalMarket = globalMarket,
            trendingCoins = trendingCoins
        ),
        PulseUiState(
            fearGreed = fearGreedGreed,
            globalMarket = globalMarket,
            trendingCoins = trendingCoins
        ),
        PulseUiState(
            errorMessageIds = setOf(R.string.error_pulse_market_mood)
        ),
        PulseUiState(
            isLoading = true
        )
    )
}

private object PulsePreviewData {
    val fearGreedFear = FearGreed(
        value = 20,
        moodBand = FearGreedMoodBand.ExtremeFear,
        history = persistentListOf(
            BigDecimal("54"),
            BigDecimal("50"),
            BigDecimal("48"),
            BigDecimal("52"),
            BigDecimal("46"),
            BigDecimal("41"),
            BigDecimal("39"),
            BigDecimal("36"),
            BigDecimal("34"),
            BigDecimal("38"),
            BigDecimal("31"),
            BigDecimal("30"),
            BigDecimal("25"),
            BigDecimal("20")
        )
    )

    val fearGreedGreed = FearGreed(
        value = 72,
        moodBand = FearGreedMoodBand.Greed,
        history = persistentListOf(
            BigDecimal("44"),
            BigDecimal("48"),
            BigDecimal("47"),
            BigDecimal("52"),
            BigDecimal("55"),
            BigDecimal("58"),
            BigDecimal("56"),
            BigDecimal("61"),
            BigDecimal("63"),
            BigDecimal("60"),
            BigDecimal("66"),
            BigDecimal("69"),
            BigDecimal("70"),
            BigDecimal("72")
        )
    )

    val globalMarket = GlobalMarket(
        totalMarketCap = Price("2410000000000", Currency.USD),
        volume24h = Price("98200000000", Currency.USD),
        btcDominancePercentage = BigDecimal("54.2"),
        coinsUp24h = 2841,
        coinsDown24h = 1893
    )

    val trendingCoins = listOf(
        TrendingCoin(
            id = "zNZHO_Sjf",
            name = "Algorand",
            symbol = "ALGO",
            imageUrl = "https://cdn.coinranking.com/fztgfHckp/algorand-algo-logo.svg",
            currentPrice = Price("0.18234", Currency.USD),
            priceChangePercentage24h = Percentage("5.42"),
            sparkline = persistentListOf(
                BigDecimal("0.171"),
                BigDecimal("0.173"),
                BigDecimal("0.176"),
                BigDecimal("0.180"),
                BigDecimal("0.182")
            )
        ),
        TrendingCoin(
            id = "K84RXBjBj",
            name = "Monero",
            symbol = "XMR",
            imageUrl = "https://cdn.coinranking.com/yiiHV-zUm/monero-xmr-logo.svg",
            currentPrice = Price("162.47", Currency.USD),
            priceChangePercentage24h = Percentage("2.18"),
            sparkline = persistentListOf()
        ),
        TrendingCoin(
            id = "a91GCGd_U96cF",
            name = "Dogecoin",
            symbol = "DOGE",
            imageUrl = "https://cdn.coinranking.com/H1arVnjQ_/doge.svg",
            currentPrice = Price("0.11923", Currency.USD),
            priceChangePercentage24h = Percentage("-1.74"),
            sparkline = persistentListOf()
        )
    ).toPersistentList()
}
