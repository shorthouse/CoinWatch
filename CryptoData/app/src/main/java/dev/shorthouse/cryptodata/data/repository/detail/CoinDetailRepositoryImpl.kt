package dev.shorthouse.cryptodata.data.repository.detail

import dev.shorthouse.cryptodata.common.Result
import dev.shorthouse.cryptodata.data.source.remote.CoinNetworkDataSource
import dev.shorthouse.cryptodata.data.source.remote.model.CoinDetailApiModel
import dev.shorthouse.cryptodata.di.IoDispatcher
import dev.shorthouse.cryptodata.model.CoinDetail
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
    override fun getCoinDetail(coinId: String): Flow<Result<CoinDetail>> = flow {
        val response = coinNetworkDataSource.getCoinDetail(coinId = coinId)

        if (response.isSuccessful) {
            val coinDetail = response.body()!!.toCoinDetail()

            emit(Result.Success(coinDetail))
        } else {
            emit(Result.Error())
        }
    }.flowOn(ioDispatcher)

    private fun CoinDetailApiModel.toCoinDetail(): CoinDetail {
        val currencyFormat: NumberFormat = NumberFormat.getCurrencyInstance(Locale.US).apply {
            currency = Currency.getInstance("USD")
            minimumFractionDigits = 2
            maximumFractionDigits = 2
        }

        val numberGroupingFormat = NumberFormat.getNumberInstance(Locale.US).apply {
            isGroupingUsed = true
        }

        val dateFormatter = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.US)

        return CoinDetail(
            id = id,
            name = name,
            symbol = symbol,
            image = image,
            currentPrice = currencyFormat.format(currentPrice),
            marketCapRank = marketCapRank,
            marketCap = currencyFormat.format(marketCap),
            circulatingSupply = numberGroupingFormat.format(circulatingSupply),
            allTimeLow = currencyFormat.format(allTimeLow),
            allTimeHigh = currencyFormat.format(allTimeHighDate),
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
