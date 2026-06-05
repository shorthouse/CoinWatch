package dev.shorthouse.coinwatch.data.repository.feargreed

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.model.FearGreed

interface FearGreedRepository {
    suspend fun getFearGreed(): Result<FearGreed>
}
