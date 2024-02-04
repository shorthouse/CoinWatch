package dev.shorthouse.coinwatch.data.mapper

import dev.shorthouse.coinwatch.data.source.remote.model.CoinDetailsApiModel
import dev.shorthouse.coinwatch.data.userPreferences.Currency
import dev.shorthouse.coinwatch.model.CoinDetails
import dev.shorthouse.coinwatch.model.Price
import java.text.NumberFormat
import java.time.DateTimeException
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

class CoinDetailsMapper @Inject constructor() {
    companion object {
        private val dateFormatter = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.US)

        private val numberGroupingFormat = NumberFormat.getNumberInstance(Locale.US).apply {
            isGroupingUsed = true
        }
    }

    fun mapApiModelToModel(apiModel: CoinDetailsApiModel, currency: Currency): CoinDetails {
        val coinDetails = apiModel.coinDetailsDataHolder?.coinDetailsData

        return CoinDetails(
            id = coinDetails?.id.orEmpty(),
            name = coinDetails?.name.orEmpty(),
            symbol = coinDetails?.symbol.orEmpty(),
            imageUrl = coinDetails?.imageUrl.orEmpty(),
            currentPrice = Price(coinDetails?.currentPrice, currency = currency),
            marketCap = Price(coinDetails?.marketCap, currency = currency),
            marketCapRank = coinDetails?.marketCapRank.orEmpty(),
            volume24h = formatNumberOrEmpty(coinDetails?.volume24h),
            circulatingSupply = formatNumberOrEmpty(coinDetails?.supply?.circulatingSupply),
            allTimeHigh = Price(coinDetails?.allTimeHigh?.price, currency = currency),
            allTimeHighDate = epochToDateOrEmpty(coinDetails?.allTimeHigh?.timestamp),
            listedDate = epochToDateOrEmpty(coinDetails?.listedAt)
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
