package dev.shorthouse.cryptodata.data.repository.detail

import dev.shorthouse.cryptodata.common.Result
import dev.shorthouse.cryptodata.data.source.remote.CoinNetworkDataSource
import dev.shorthouse.cryptodata.data.source.remote.model.CoinDetailApiModel
import dev.shorthouse.cryptodata.data.source.remote.model.CoinChartApiModel
import dev.shorthouse.cryptodata.di.IoDispatcher
import dev.shorthouse.cryptodata.model.CoinDetail
import dev.shorthouse.cryptodata.model.Percentage
import dev.shorthouse.cryptodata.model.Price
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class CoinDetailRepositoryImpl @Inject constructor(
    private val coinNetworkDataSource: CoinNetworkDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : CoinDetailRepository {
    override fun getCoinDetail(
        coinId: String,
        periodDays: String
    ): Flow<Result<CoinDetail>> = flow {
        val coinDetailResponse = coinNetworkDataSource.getCoinDetail(coinId = coinId)

        val coinPastPricesResponse = coinNetworkDataSource.getCoinChart(
            coinId = coinId,
            chartPeriodDays = periodDays
        )

        if (coinDetailResponse.isSuccessful && coinPastPricesResponse.isSuccessful) {
            val coinDetailApiModel = coinDetailResponse.body()!!
            val coinPastPricesApiModel = coinPastPricesResponse.body()!!

            val coinDetail = createCoinDetail(
                coinDetailApiModel = coinDetailApiModel,
                coinPastPricesApiModel = coinPastPricesApiModel
            )

            emit(Result.Success(coinDetail))
        } else {
            emit(Result.Error())
        }
    }.flowOn(ioDispatcher)

    private fun createCoinDetail(
        coinDetailApiModel: CoinDetailApiModel,
        coinPastPricesApiModel: CoinChartApiModel
    ): CoinDetail {
        val numberGroupingFormat = NumberFormat.getNumberInstance(Locale.US).apply {
            isGroupingUsed = true
        }

        val dateFormatter = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.US)

        val pastPrices = coinPastPricesApiModel.prices.map { it.last() }
        val currentPrice = coinDetailApiModel.marketData.currentPrice.usd

        val minPastPrice = pastPrices.min()
        val minPriceChangePercentage = ((currentPrice - minPastPrice) / minPastPrice) * 100

        val maxPastPrice = pastPrices.max()
        val maxPriceChangePercentage = ((currentPrice - maxPastPrice) / maxPastPrice) * 100

        val oldestPastPrice = pastPrices.first()
        val periodPriceChangePercentage = ((currentPrice - oldestPastPrice) / oldestPastPrice) * 100

        return CoinDetail(
            id = coinDetailApiModel.id
            name = coinDetailApiModel.name,
            symbol = coinDetailApiModel.symbol.uppercase(),
            image = coinDetailApiModel.image.large,
            currentPrice = Price(coinDetailApiModel.marketData.currentPrice.usd),
            marketCapRank = numberGroupingFormat.format(
                coinDetailApiModel.marketData.marketCapRank
            ),
            marketCap = Price(coinDetailApiModel.marketData.marketCap.usd),
            circulatingSupply = numberGroupingFormat.format(
                coinDetailApiModel.marketData.circulatingSupply
            ),
            allTimeLow = Price(coinDetailApiModel.marketData.allTimeLow.usd),
            allTimeHigh = Price(coinDetailApiModel.marketData.allTimeHigh.usd),
            allTimeLowDate = dateFormatter.format(
                LocalDateTime.parse(
                    coinDetailApiModel.marketData.allTimeLowDate.usd,
                    DateTimeFormatter.ISO_DATE_TIME
                )
            ),
            allTimeHighDate = dateFormatter.format(
                LocalDateTime.parse(
                    coinDetailApiModel.marketData.allTimeHighDate.usd,
                    DateTimeFormatter.ISO_DATE_TIME
                )
            ),
            pastPrices = pastPrices,
            minPastPrice = Price(minPastPrice),
            minPriceChangePercentage = Percentage(minPriceChangePercentage),
            maxPastPrice = Price(maxPastPrice),
            maxPriceChangePercentage = Percentage(maxPriceChangePercentage),
            periodPriceChangePercentage = Percentage(periodPriceChangePercentage)
        )
    }
}
