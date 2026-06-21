package dev.shorthouse.coinwatch.data.repository.fearGreed

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.model.FearGreed

interface FearGreedRepository {
    suspend fun getFearGreed(): Result<FearGreed>
}
