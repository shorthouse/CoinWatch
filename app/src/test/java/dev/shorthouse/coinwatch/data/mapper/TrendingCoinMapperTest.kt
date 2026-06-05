package dev.shorthouse.coinwatch.data.mapper

import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.data.source.local.datastore.global.Currency
import dev.shorthouse.coinwatch.data.source.remote.model.TrendingCoinApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.TrendingCoinsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.TrendingCoinsData
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.Price
import dev.shorthouse.coinwatch.model.TrendingCoin
import java.math.BigDecimal
import kotlinx.collections.immutable.persistentListOf
import org.junit.Test

class TrendingCoinMapperTest {

    private val trendingCoinMapper = TrendingCoinMapper()

    @Test
    fun `When trending coins data is valid should map values`() {
        val apiModel = trendingCoinsApiModel(
            trendingCoinApiModel(
                id = "Qwsogvtv82FCd",
                symbol = "BTC",
                name = "Bitcoin",
                imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                currentPrice = "29446.336548759988",
                priceChangePercentage24h = "1.76833",
                rank = 1,
                sparkline = listOf("29100", "29250", "29446")
            )
        )

        val result = trendingCoinMapper.mapApiModelToModel(apiModel, currency = Currency.USD)

        assertThat(result).containsExactly(
            TrendingCoin(
                id = "Qwsogvtv82FCd",
                name = "Bitcoin",
                symbol = "BTC",
                imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                currentPrice = Price("29446.336548759988", currency = Currency.USD),
                priceChangePercentage24h = Percentage("1.76833"),
                sparkline = persistentListOf(
                    BigDecimal("29100"),
                    BigDecimal("29250"),
                    BigDecimal("29446")
                )
            )
        )
    }

    @Test
    fun `When currency provided should preserve currency in prices`() {
        val apiModel = trendingCoinsApiModel(trendingCoinApiModel())

        val result = trendingCoinMapper.mapApiModelToModel(apiModel, currency = Currency.GBP)

        assertThat(result.first().currentPrice).isEqualTo(
            Price("29446.336548759988", currency = Currency.GBP)
        )
    }

    @Test
    fun `When coins are unordered should sort by rank ascending`() {
        val apiModel = trendingCoinsApiModel(
            trendingCoinApiModel(id = "third", rank = 3),
            trendingCoinApiModel(id = "first", rank = 1),
            trendingCoinApiModel(id = "second", rank = 2)
        )

        val result = trendingCoinMapper.mapApiModelToModel(apiModel, currency = Currency.USD)

        assertThat(result.map { it.id })
            .containsExactly("first", "second", "third")
            .inOrder()
    }

    @Test
    fun `When more than ten coins provided should take only top ten by rank`() {
        val apiModel = trendingCoinsApiModel(
            *(1..15).map { rank ->
                trendingCoinApiModel(id = "coin-$rank", rank = rank)
            }.toTypedArray()
        )

        val result = trendingCoinMapper.mapApiModelToModel(apiModel, currency = Currency.USD)

        assertThat(result).hasSize(10)
        assertThat(result.map { it.id }).containsExactly(
            "coin-1", "coin-2", "coin-3", "coin-4", "coin-5",
            "coin-6", "coin-7", "coin-8", "coin-9", "coin-10"
        ).inOrder()
    }

    @Test
    fun `When coins have null id should filter them out`() {
        val apiModel = trendingCoinsApiModel(
            trendingCoinApiModel(id = null, rank = 1),
            trendingCoinApiModel(id = "valid", rank = 2)
        )

        val result = trendingCoinMapper.mapApiModelToModel(apiModel, currency = Currency.USD)

        assertThat(result.map { it.id }).containsExactly("valid")
    }

    @Test
    fun `When coins list is null should return empty list`() {
        val apiModel = TrendingCoinsApiModel(trendingCoinsData = TrendingCoinsData(coins = null))

        val result = trendingCoinMapper.mapApiModelToModel(apiModel, currency = Currency.USD)

        assertThat(result).isEmpty()
    }

    @Test
    fun `When data is null should return empty list`() {
        val apiModel = TrendingCoinsApiModel(trendingCoinsData = null)

        val result = trendingCoinMapper.mapApiModelToModel(apiModel, currency = Currency.USD)

        assertThat(result).isEmpty()
    }

    @Test
    fun `When sparkline has invalid entries should keep only valid prices`() {
        val apiModel = trendingCoinsApiModel(
            trendingCoinApiModel(
                sparkline = listOf("29100", null, "not-a-number", "29446")
            )
        )

        val result = trendingCoinMapper.mapApiModelToModel(apiModel, currency = Currency.USD)

        assertThat(result.first().sparkline).containsExactly(
            BigDecimal("29100"),
            BigDecimal("29446")
        ).inOrder()
    }

    private fun trendingCoinsApiModel(vararg coins: TrendingCoinApiModel) = TrendingCoinsApiModel(
        trendingCoinsData = TrendingCoinsData(coins = coins.toList())
    )

    private fun trendingCoinApiModel(
        id: String? = "Qwsogvtv82FCd",
        symbol: String? = "BTC",
        name: String? = "Bitcoin",
        imageUrl: String? = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
        currentPrice: String? = "29446.336548759988",
        priceChangePercentage24h: String? = "1.76833",
        rank: Int? = 1,
        sparkline: List<String?>? = listOf("29100", "29250", "29446"),
    ) = TrendingCoinApiModel(
        id = id,
        symbol = symbol,
        name = name,
        imageUrl = imageUrl,
        currentPrice = currentPrice,
        priceChangePercentage24h = priceChangePercentage24h,
        rank = rank,
        sparkline = sparkline
    )
}
