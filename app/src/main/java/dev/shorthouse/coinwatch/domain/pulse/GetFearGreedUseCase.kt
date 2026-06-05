package dev.shorthouse.coinwatch.domain.pulse

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.repository.feargreed.FearGreedRepository
import dev.shorthouse.coinwatch.model.FearGreed
import javax.inject.Inject

class GetFearGreedUseCase @Inject constructor(
    private val fearGreedRepository: FearGreedRepository
) {
    suspend operator fun invoke(): Result<FearGreed> {
        return getFearGreed()
    }

    private suspend fun getFearGreed(): Result<FearGreed> {
        return fearGreedRepository.getFearGreed()
    }
}
