package dev.shorthouse.coinwatch.domain.pulse

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.repository.movers.MoversRepository
import dev.shorthouse.coinwatch.data.source.local.datastore.global.Currency
import dev.shorthouse.coinwatch.model.Movers
import javax.inject.Inject

class GetMoversUseCase @Inject constructor(
    private val moversRepository: MoversRepository,
) {
    suspend operator fun invoke(currency: Currency): Result<Movers> {
        return getMovers(currency = currency)
    }

    private suspend fun getMovers(currency: Currency): Result<Movers> {
        return moversRepository.getMovers(currency = currency)
    }
}
