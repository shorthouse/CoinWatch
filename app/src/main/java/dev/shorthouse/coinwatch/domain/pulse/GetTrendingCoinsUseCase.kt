package dev.shorthouse.coinwatch.domain.pulse

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.repository.trendingCoins.TrendingCoinsRepository
import dev.shorthouse.coinwatch.data.source.local.datastore.global.Currency
import dev.shorthouse.coinwatch.model.TrendingCoin
import javax.inject.Inject

class GetTrendingCoinsUseCase @Inject constructor(
    private val trendingCoinsRepository: TrendingCoinsRepository,
) {
    suspend operator fun invoke(currency: Currency): Result<List<TrendingCoin>> {
        return getTrendingCoins(currency = currency)
    }

    private suspend fun getTrendingCoins(currency: Currency): Result<List<TrendingCoin>> {
        return trendingCoinsRepository.getTrendingCoins(currency = currency)
    }
}
