package dev.shorthouse.coinwatch.data.mapper

import dev.shorthouse.coinwatch.common.toSanitisedBigDecimalOrNull
import dev.shorthouse.coinwatch.data.source.local.datastore.global.Currency
import dev.shorthouse.coinwatch.data.source.remote.model.GlobalMarketCoinStatsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.GlobalStatsApiModel
import dev.shorthouse.coinwatch.model.GlobalMarket
import dev.shorthouse.coinwatch.model.Price
import java.math.BigDecimal
import javax.inject.Inject

class GlobalMarketMapper @Inject constructor() {
    fun mapApiModelsToModel(
        globalStatsApiModel: GlobalStatsApiModel,
        globalMarketCoinStatsApiModel: GlobalMarketCoinStatsApiModel,
        currency: Currency,
    ): GlobalMarket? {
        val globalStats = globalStatsApiModel.data ?: return null
        val coinStats = globalMarketCoinStatsApiModel.data?.stats ?: return null

        val totalMarketCap = globalStats.totalMarketCap
            .takeIf { it.toSanitisedBigDecimalOrNull() != null }
            ?: return null

        val total24hVolume = globalStats.total24hVolume
            .takeIf { it.toSanitisedBigDecimalOrNull() != null }
            ?: return null

        val btcDominance = globalStats.btcDominance
            ?.let { BigDecimal.valueOf(it) }
            ?: return null

        val positiveChanges = coinStats.positiveChanges ?: return null
        val negativeChanges = coinStats.negativeChanges ?: return null

        return GlobalMarket(
            totalMarketCap = Price(totalMarketCap, currency = currency),
            volume24h = Price(total24hVolume, currency = currency),
            btcDominancePercentage = btcDominance,
            coinsUp24h = positiveChanges,
            coinsDown24h = negativeChanges,
        )
    }
}
