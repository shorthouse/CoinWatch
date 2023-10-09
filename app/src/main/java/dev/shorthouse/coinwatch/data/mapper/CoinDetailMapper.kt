package dev.shorthouse.coinwatch.data.mapper

import dev.shorthouse.coinwatch.common.Mapper
import dev.shorthouse.coinwatch.data.source.remote.model.CoinDetailApiModel
import dev.shorthouse.coinwatch.model.CoinDetail
import dev.shorthouse.coinwatch.model.Price
import java.text.NumberFormat
import java.time.DateTimeException
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

class CoinDetailMapper @Inject constructor() : Mapper<CoinDetailApiModel, CoinDetail> {
    companion object {
        private val dateFormatter = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.US)

        private val numberGroupingFormat = NumberFormat.getNumberInstance(Locale.US).apply {
            isGroupingUsed = true
        }
    }

    override fun mapApiModelToModel(from: CoinDetailApiModel): CoinDetail {
        val coinDetail = from.coinDetailDataHolder?.coinDetailData

        return CoinDetail(
            id = coinDetail?.id.orEmpty(),
            name = coinDetail?.name.orEmpty(),
            symbol = coinDetail?.symbol.orEmpty(),
            imageUrl = coinDetail?.imageUrl.orEmpty(),
            currentPrice = Price(coinDetail?.currentPrice),
            marketCap = Price(coinDetail?.marketCap),
            marketCapRank = coinDetail?.marketCapRank.orEmpty(),
            volume24h = formatNumberOrEmpty(coinDetail?.volume24h),
            circulatingSupply = formatNumberOrEmpty(coinDetail?.supply?.circulatingSupply),
            allTimeHigh = Price(coinDetail?.allTimeHigh?.price),
            allTimeHighDate = epochToDateOrEmpty(coinDetail?.allTimeHigh?.timestamp),
            listedDate = epochToDateOrEmpty(coinDetail?.listedAt)
        )
    }

    private fun epochToDateOrEmpty(epochSecond: Long?): String {
        try {
            if (epochSecond == null || epochSecond < 0) return ""

            val epochInstant = Instant.ofEpochSecond(epochSecond)
                .atZone(ZoneId.systemDefault())

            return dateFormatter.format(epochInstant)
        } catch (e: DateTimeException) {
            return ""
        }
    }

    private fun formatNumberOrEmpty(numberString: String?): String {
        val number = numberString?.toDoubleOrNull() ?: return ""

        return numberGroupingFormat.format(number)
    }
}
