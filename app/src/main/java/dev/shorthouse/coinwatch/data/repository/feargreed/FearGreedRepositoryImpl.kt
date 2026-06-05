package dev.shorthouse.coinwatch.data.repository.feargreed

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.mapper.FearGreedMapper
import dev.shorthouse.coinwatch.data.source.remote.CoinNetworkDataSource
import dev.shorthouse.coinwatch.model.FearGreed
import timber.log.Timber
import javax.inject.Inject

class FearGreedRepositoryImpl @Inject constructor(
    private val coinNetworkDataSource: CoinNetworkDataSource,
    private val fearGreedMapper: FearGreedMapper,
) : FearGreedRepository {
    override suspend fun getFearGreed(): Result<FearGreed> {
        return try {
            val response = coinNetworkDataSource.getFearGreed()
            val body = response.body()
            val fearGreed = body?.let(fearGreedMapper::mapApiModelToModel)

            if (response.isSuccessful && body?.data != null && fearGreed != null) {
                Result.Success(fearGreed)
            } else {
                Timber.e(
                    "getFearGreed unsuccessful retrofit response ${response.message()}"
                )
                Result.Error(ERROR_MESSAGE)
            }
        } catch (e: Exception) {
            Timber.e("getFearGreed error ${e.message}")
            Result.Error(ERROR_MESSAGE)
        }
    }

    private companion object {
        const val ERROR_MESSAGE = "Unable to fetch fear and greed"
    }
}
