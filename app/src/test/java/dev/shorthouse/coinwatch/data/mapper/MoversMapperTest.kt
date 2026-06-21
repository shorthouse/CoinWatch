package dev.shorthouse.coinwatch.data.mapper

import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.data.source.local.datastore.global.Currency
import dev.shorthouse.coinwatch.data.source.remote.model.MoverCoinApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.MoversApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.MoversData
import dev.shorthouse.coinwatch.model.MoverCoin
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.Price
import java.math.BigDecimal
import kotlinx.collections.immutable.persistentListOf
import org.junit.Test

class MoversMapperTest {

    private val moversMapper = MoversMapper()

    @Test
    fun `When gainers and losers are valid should map top gainer and top loser`() {
        val gainers = moversApiModel(
            moverCoinApiModel(
                id = "Qwsogvtv82FCd",
                symbol = "BTC",
                name = "Bitcoin",
                imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                currentPrice = "29446.336548759988",
                priceChangePercentage24h = "14.27",
                sparkline = listOf("29100", "29250", "29446")
            )
        )
        val losers = moversApiModel(
            moverCoinApiModel(
                id = "a91GCGd_U96cF",
                symbol = "DOGE",
                name = "Dogecoin",
                imageUrl = "https://cdn.coinranking.com/H1arVnjQ_/doge.svg",
                currentPrice = "0.11923",
                priceChangePercentage24h = "-11.62",
                sparkline = listOf("0.13", "0.12", "0.11")
            )
        )

        val result = moversMapper.mapApiModelToModel(gainers, losers, currency = Currency.USD)

        assertThat(result).isNotNull()
        assertThat(result!!.topGainer).isEqualTo(
            MoverCoin(
                id = "Qwsogvtv82FCd",
                name = "Bitcoin",
                symbol = "BTC",
                imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                currentPrice = Price("29446.336548759988", currency = Currency.USD),
                priceChangePercentage24h = Percentage("14.27"),
                sparkline = persistentListOf(
                    BigDecimal("29100"),
                    BigDecimal("29250"),
                    BigDecimal("29446")
                )
            )
        )
        assertThat(result.topLoser.id).isEqualTo("a91GCGd_U96cF")
        assertThat(result.topLoser.symbol).isEqualTo("DOGE")
    }

    @Test
    fun `When top gainer is first gainer and top loser is first loser`() {
        val gainers = moversApiModel(
            moverCoinApiModel(id = "g1", priceChangePercentage24h = "20"),
            moverCoinApiModel(id = "g2", priceChangePercentage24h = "10")
        )
        val losers = moversApiModel(
            moverCoinApiModel(id = "l1", priceChangePercentage24h = "-18"),
            moverCoinApiModel(id = "l2", priceChangePercentage24h = "-9")
        )

        val result = moversMapper.mapApiModelToModel(gainers, losers, currency = Currency.USD)

        assertThat(result!!.topGainer.id).isEqualTo("g1")
        assertThat(result.topLoser.id).isEqualTo("l1")
    }

    @Test
    fun `When deriving most movement should take next five by absolute change excluding featured`() {
        val gainers = moversApiModel(
            moverCoinApiModel(id = "g1", priceChangePercentage24h = "30"),
            moverCoinApiModel(id = "g2", priceChangePercentage24h = "12"),
            moverCoinApiModel(id = "g3", priceChangePercentage24h = "8"),
            moverCoinApiModel(id = "g4", priceChangePercentage24h = "5"),
            moverCoinApiModel(id = "g5", priceChangePercentage24h = "2")
        )
        val losers = moversApiModel(
            moverCoinApiModel(id = "l1", priceChangePercentage24h = "-25"),
            moverCoinApiModel(id = "l2", priceChangePercentage24h = "-15"),
            moverCoinApiModel(id = "l3", priceChangePercentage24h = "-3")
        )

        val result = moversMapper.mapApiModelToModel(gainers, losers, currency = Currency.USD)

        // Featured: g1 (+30) and l1 (-25). Remaining sorted by |change| desc:
        // l2(15), g2(12), g3(8), g4(5), l3(3), g5(2) -> take 5
        assertThat(result!!.mostMovement.map { it.id })
            .containsExactly("l2", "g2", "g3", "g4", "l3")
            .inOrder()
    }

    @Test
    fun `When a coin appears in both lists should not duplicate in most movement`() {
        val shared = moverCoinApiModel(id = "shared", priceChangePercentage24h = "4")
        val gainers = moversApiModel(
            moverCoinApiModel(id = "g1", priceChangePercentage24h = "30"),
            shared
        )
        val losers = moversApiModel(
            moverCoinApiModel(id = "l1", priceChangePercentage24h = "-25"),
            shared
        )

        val result = moversMapper.mapApiModelToModel(gainers, losers, currency = Currency.USD)

        assertThat(result!!.mostMovement.map { it.id }).containsExactly("shared")
    }

    @Test
    fun `When currency provided should preserve currency in prices`() {
        val gainers = moversApiModel(moverCoinApiModel(id = "g1"))
        val losers = moversApiModel(moverCoinApiModel(id = "l1"))

        val result = moversMapper.mapApiModelToModel(gainers, losers, currency = Currency.GBP)

        assertThat(result!!.topGainer.currentPrice)
            .isEqualTo(Price("29446.336548759988", currency = Currency.GBP))
    }

    @Test
    fun `When coins have null id should filter them out`() {
        val gainers = moversApiModel(
            moverCoinApiModel(id = null, priceChangePercentage24h = "40"),
            moverCoinApiModel(id = "g1", priceChangePercentage24h = "30")
        )
        val losers = moversApiModel(moverCoinApiModel(id = "l1"))

        val result = moversMapper.mapApiModelToModel(gainers, losers, currency = Currency.USD)

        assertThat(result!!.topGainer.id).isEqualTo("g1")
    }

    @Test
    fun `When sparkline has invalid entries should keep only valid prices`() {
        val gainers = moversApiModel(
            moverCoinApiModel(id = "g1", sparkline = listOf("29100", null, "not-a-number", "29446"))
        )
        val losers = moversApiModel(moverCoinApiModel(id = "l1"))

        val result = moversMapper.mapApiModelToModel(gainers, losers, currency = Currency.USD)

        assertThat(result!!.topGainer.sparkline).containsExactly(
            BigDecimal("29100"),
            BigDecimal("29446")
        ).inOrder()
    }

    @Test
    fun `When gainers list is empty should return null`() {
        val gainers = MoversApiModel(moversData = MoversData(coins = emptyList()))
        val losers = moversApiModel(moverCoinApiModel(id = "l1"))

        val result = moversMapper.mapApiModelToModel(gainers, losers, currency = Currency.USD)

        assertThat(result).isNull()
    }

    @Test
    fun `When losers list is empty should return null`() {
        val gainers = moversApiModel(moverCoinApiModel(id = "g1"))
        val losers = MoversApiModel(moversData = MoversData(coins = emptyList()))

        val result = moversMapper.mapApiModelToModel(gainers, losers, currency = Currency.USD)

        assertThat(result).isNull()
    }

    @Test
    fun `When data is null should return null`() {
        val gainers = MoversApiModel(moversData = null)
        val losers = MoversApiModel(moversData = null)

        val result = moversMapper.mapApiModelToModel(gainers, losers, currency = Currency.USD)

        assertThat(result).isNull()
    }

    private fun moversApiModel(vararg coins: MoverCoinApiModel) = MoversApiModel(
        moversData = MoversData(coins = coins.toList())
    )

    private fun moverCoinApiModel(
        id: String? = "Qwsogvtv82FCd",
        symbol: String? = "BTC",
        name: String? = "Bitcoin",
        imageUrl: String? = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
        currentPrice: String? = "29446.336548759988",
        priceChangePercentage24h: String? = "1.76833",
        sparkline: List<String?>? = listOf("29100", "29250", "29446"),
    ) = MoverCoinApiModel(
        id = id,
        symbol = symbol,
        name = name,
        imageUrl = imageUrl,
        currentPrice = currentPrice,
        priceChangePercentage24h = priceChangePercentage24h,
        sparkline = sparkline
    )
}
