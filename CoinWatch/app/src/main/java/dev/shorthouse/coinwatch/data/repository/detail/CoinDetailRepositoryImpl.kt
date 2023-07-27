package dev.shorthouse.coinwatch.data.repository.detail

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.source.remote.CoinNetworkDataSource
import dev.shorthouse.coinwatch.data.source.remote.model.CoinDetailApiModel
import dev.shorthouse.coinwatch.di.IoDispatcher
import dev.shorthouse.coinwatch.model.CoinDetail
import dev.shorthouse.coinwatch.model.Price
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

class CoinDetailRepositoryImpl @Inject constructor(
    private val coinNetworkDataSource: CoinNetworkDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : CoinDetailRepository {
    override fun getCoinDetail(coinId: String): Flow<Result<CoinDetail>> = flow {
        emit(
            try {
                val response = coinNetworkDataSource.getCoinDetail(coinId = coinId)
                val body = response.body()

                if (response.isSuccessful && body != null) {
                    Result.Success(body.first().toCoinDetail())
                } else {
                    Timber.e("getCoinDetail unsuccessful retrofit response ${response.message()}")
                    Result.Error(message = "Unable to fetch coin details")
                }
            } catch (e: Throwable) {
                Timber.e("getCoinDetail exception ${e.message}")
                Result.Error(message = "Unable to fetch coin details")
            }
        )
    }.flowOn(ioDispatcher)

    private fun CoinDetailApiModel.toCoinDetail(): CoinDetail {
        val numberGroupingFormat = NumberFormat.getNumberInstance(Locale.US).apply {
            isGroupingUsed = true
        }

        val dateFormatter = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.US)

        return CoinDetail(
            id = id,
            name = name.orEmpty(),
            symbol = symbol?.uppercase().orEmpty(),
            image = image.orEmpty(),
            currentPrice = Price(currentPrice),
            marketCapRank = marketCapRank?.toString().orEmpty(),
            marketCap = Price(marketCap),
            circulatingSupply = numberGroupingFormat.format(circulatingSupply),
            allTimeLow = Price(allTimeLow),
            allTimeHigh = Price(allTimeHigh),
            allTimeLowDate = dateFormatter.format(
                LocalDateTime.parse(
                    allTimeLowDate,
                    DateTimeFormatter.ISO_DATE_TIME
                )
            ),
            allTimeHighDate = dateFormatter.format(
                LocalDateTime.parse(
                    allTimeHighDate,
                    DateTimeFormatter.ISO_DATE_TIME
                )
            )
        )
    }
}
