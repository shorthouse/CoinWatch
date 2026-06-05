package dev.shorthouse.coinwatch.domain.pulse

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.repository.globalmarket.GlobalMarketRepository
import dev.shorthouse.coinwatch.data.source.local.datastore.global.Currency
import dev.shorthouse.coinwatch.model.GlobalMarket
import javax.inject.Inject

class GetGlobalMarketUseCase @Inject constructor(
    private val globalMarketRepository: GlobalMarketRepository,
) {
    suspend operator fun invoke(currency: Currency): Result<GlobalMarket> {
        return getGlobalMarket(currency = currency)
    }

    private suspend fun getGlobalMarket(currency: Currency): Result<GlobalMarket> {
        return globalMarketRepository.getGlobalMarket(currency = currency)
    }
}
