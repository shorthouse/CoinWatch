package dev.shorthouse.coinwatch.data.mapper

import dev.shorthouse.coinwatch.common.Constants.MISSING_VALUE_PLACEHOLDER
import dev.shorthouse.coinwatch.data.source.local.datastore.global.Currency
import dev.shorthouse.coinwatch.data.source.remote.model.CoinDetailsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.CoinDetailsData
import dev.shorthouse.coinwatch.data.source.remote.model.CoinDetailsLink
import dev.shorthouse.coinwatch.model.CoinDetails
import dev.shorthouse.coinwatch.model.CoinLink
import dev.shorthouse.coinwatch.model.CoinLinkType
import dev.shorthouse.coinwatch.model.Price
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import java.net.URI
import java.net.URISyntaxException
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

        private val linkTypes = mapOf(
            "website" to CoinLinkType.Website,
            "whitepaper" to CoinLinkType.Whitepaper,
            "discord" to CoinLinkType.Discord,
            "github" to CoinLinkType.GitHub,
            "reddit" to CoinLinkType.Reddit,
            "telegram" to CoinLinkType.Telegram,
            "twitter" to CoinLinkType.X,
            "youtube" to CoinLinkType.YouTube,
        )
    }

    fun mapApiModelToModel(apiModel: CoinDetailsApiModel, currency: Currency): CoinDetails {
        val coinDetails = apiModel.coinDetailsDataHolder?.coinDetailsData

        return CoinDetails(
            id = coinDetails?.id.orEmpty(),
            name = coinDetails?.name.orEmpty(),
            symbol = coinDetails?.symbol.orEmpty(),
            description = coinDetails?.description.orEmpty(),
            tags = coinDetails?.tags
                ?.map { tag -> tag.trim() }
                ?.filter { tag -> tag.isNotEmpty() }
                ?.toImmutableList()
                ?: persistentListOf(),
            links = coinDetails?.let { details -> mapLinks(details) } ?: persistentListOf(),
            imageUrl = coinDetails?.imageUrl.orEmpty(),
            currentPrice = Price(coinDetails?.currentPrice, currency = currency),
            marketCap = Price(coinDetails?.marketCap, currency = currency),
            fullyDilutedMarketCap = Price(coinDetails?.fullyDilutedMarketCap, currency = currency),
            marketCapRank = coinDetails?.marketCapRank.orPlaceholder(),
            volume24h = Price(coinDetails?.volume24h, currency = currency),
            numberOfExchanges = formatNumberOrPlaceholder(
                coinDetails?.numberOfExchanges?.toString()
            ),
            numberOfMarkets = formatNumberOrPlaceholder(
                coinDetails?.numberOfMarkets?.toString()
            ),
            circulatingSupply = formatNumberOrPlaceholder(
                coinDetails?.supply?.circulatingSupply
            ),
            totalSupply = formatNumberOrPlaceholder(
                coinDetails?.supply?.totalSupply
            ),
            maxSupply = formatNumberOrPlaceholder(
                coinDetails?.supply?.maxSupply
            ),
            allTimeHigh = Price(coinDetails?.allTimeHigh?.price, currency = currency),
            allTimeHighDate = epochToDateOrPlaceholder(
                coinDetails?.allTimeHigh?.timestamp
            ),
            listedDate = epochToDateOrPlaceholder(coinDetails?.listedAt)
        )
    }

    private fun String?.orPlaceholder(): String =
        this?.takeIf { value -> value.isNotBlank() } ?: MISSING_VALUE_PLACEHOLDER

    private fun epochToDateOrPlaceholder(epochSecond: Long?): String =
        epochToDateOrNull(epochSecond).orPlaceholder()

    private fun epochToDateOrNull(epochSecond: Long?): String? {
        try {
            if (epochSecond == null || epochSecond < 0) return null

            val epochInstant = Instant.ofEpochSecond(epochSecond)
                .atZone(ZoneId.systemDefault())

            return dateFormatter.format(epochInstant)
        } catch (e: DateTimeException) {
            return null
        }
    }

    private fun formatNumberOrPlaceholder(numberString: String?): String =
        formatNumberOrNull(numberString).orPlaceholder()

    private fun formatNumberOrNull(numberString: String?): String? {
        val number = numberString?.toDoubleOrNull() ?: return null

        return numberGroupingFormat.format(number)
    }

    private fun mapLinks(coinDetails: CoinDetailsData): ImmutableList<CoinLink> {
        val links = mutableListOf<CoinLink>()
        val seenCoinLinkTypes = mutableSetOf<CoinLinkType>()

        links.addLinkIfUnique(
            type = CoinLinkType.Website,
            url = coinDetails.websiteUrl,
            seenCoinLinkTypes = seenCoinLinkTypes
        )

        coinDetails.links.orEmpty().forEach { link ->
            val type = link.toLinkType() ?: return@forEach

            links.addLinkIfUnique(
                type = type,
                url = link.url,
                seenCoinLinkTypes = seenCoinLinkTypes
            )
        }

        return links.toImmutableList()
    }

    private fun MutableList<CoinLink>.addLinkIfUnique(
        type: CoinLinkType,
        url: String?,
        seenCoinLinkTypes: MutableSet<CoinLinkType>,
    ) {
        val trimmedUrl = url?.trim().orEmpty()
        if (
            trimmedUrl.isEmpty() ||
            seenCoinLinkTypes.contains(type) ||
            !trimmedUrl.isValidHttpUrl()
        ) {
            return
        }

        add(
            CoinLink(
                type = type,
                url = trimmedUrl
            )
        )

        seenCoinLinkTypes.add(type)
    }

    private fun CoinDetailsLink.toLinkType(): CoinLinkType? {
        val type = this.type?.trim()?.lowercase()

        return if (type.isNullOrBlank()) {
            null
        } else {
            linkTypes[type]
        }
    }

    private fun String.isValidHttpUrl(): Boolean {
        return try {
            val uri = URI(this)
            val scheme = uri.scheme?.lowercase(Locale.US)
            scheme in setOf("http", "https") && !uri.host.isNullOrBlank()
        } catch (e: URISyntaxException) {
            false
        } catch (e: IllegalArgumentException) {
            false
        }
    }
}
