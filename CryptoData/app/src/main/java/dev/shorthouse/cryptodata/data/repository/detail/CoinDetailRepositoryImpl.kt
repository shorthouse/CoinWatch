package dev.shorthouse.cryptodata.data.repository.detail

import dev.shorthouse.cryptodata.common.Result
import dev.shorthouse.cryptodata.data.source.remote.CoinNetworkDataSource
import dev.shorthouse.cryptodata.data.source.remote.model.CoinDetailApiModel
import dev.shorthouse.cryptodata.data.source.remote.model.CoinPastPricesApiModel
import dev.shorthouse.cryptodata.di.IoDispatcher
import dev.shorthouse.cryptodata.model.CoinDetail
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Currency
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

        val coinPastPricesResponse = coinNetworkDataSource.getCoinPastPrices(
            coinId = coinId,
            periodDays = periodDays
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
        coinPastPricesApiModel: CoinPastPricesApiModel
    ): CoinDetail {
        val currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US).apply {
            currency = Currency.getInstance("USD")
        }

        val decimalFormatter = DecimalFormat(
            "#,###",
            DecimalFormatSymbols.getInstance(Locale.US)
        )

        val dateFormatter = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.US)

        val pastPrices = coinPastPricesApiModel.prices.map { it.last() }

        val minPrice = pastPrices.min()
        val maxPrice = pastPrices.max()
        val currentPrice = coinDetailApiModel.marketData.currentPrice.usd

        val minPriceChangePercentage = ((currentPrice - minPrice) / minPrice) * 100
        val maxPriceChangePercentage = ((currentPrice - maxPrice) / maxPrice) * 100

        return CoinDetail(
            id = coinDetailApiModel.id,
            name = coinDetailApiModel.name,
            symbol = coinDetailApiModel.symbol.uppercase(),
            image = coinDetailApiModel.image.large,
            currentPrice = currencyFormatter.format(
                coinDetailApiModel.marketData.currentPrice.usd
            ),
            marketCapRank = coinDetailApiModel.marketData.marketCapRank,
            marketCap = currencyFormatter.format(
                coinDetailApiModel.marketData.marketCap.usd
            ),
            circulatingSupply = decimalFormatter.format(
                coinDetailApiModel.marketData.circulatingSupply
            ),
            allTimeLow = currencyFormatter.format(
                coinDetailApiModel.marketData.allTimeLow.usd
            ),
            allTimeHigh = currencyFormatter.format(
                coinDetailApiModel.marketData.allTimeHigh.usd
            ),
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
            minPrice = minPrice,
            minPriceFormatted = currencyFormatter.format(minPrice),
            minPriceChangePercentage = minPriceChangePercentage,
            maxPrice = maxPrice,
            maxPriceFormatted = currencyFormatter.format(maxPrice),
            maxPriceChangePercentage = maxPriceChangePercentage
        )
    }
}
