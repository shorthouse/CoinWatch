package dev.shorthouse.coinwatch.data.mapper

import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.data.source.local.datastore.global.Currency
import dev.shorthouse.coinwatch.data.source.remote.model.GlobalMarketCoinStatsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.GlobalMarketCoinStatsData
import dev.shorthouse.coinwatch.data.source.remote.model.GlobalMarketCoinStatsDataHolder
import dev.shorthouse.coinwatch.data.source.remote.model.GlobalStatsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.GlobalStatsData
import dev.shorthouse.coinwatch.model.GlobalMarket
import dev.shorthouse.coinwatch.model.Price
import java.math.BigDecimal
import org.junit.Test

class GlobalMarketMapperTest {

    private val globalMarketMapper = GlobalMarketMapper()

    @Test
    fun `When global market data is valid should map values`() {
        val result = globalMarketMapper.mapApiModelsToModel(
            globalStatsApiModel = globalStatsApiModel(),
            globalMarketCoinStatsApiModel = globalMarketCoinStatsApiModel(),
            currency = Currency.USD
        )

        assertThat(result).isEqualTo(
            GlobalMarket(
                totalMarketCap = Price("2410000000000", currency = Currency.USD),
                volume24h = Price("98200000000", currency = Currency.USD),
                btcDominancePercentage = BigDecimal("54.2"),
                coinsUp24h = 2841,
                coinsDown24h = 1893
            )
        )
    }

    @Test
    fun `When currency provided should preserve currency in prices`() {
        val result = globalMarketMapper.mapApiModelsToModel(
            globalStatsApiModel = globalStatsApiModel(),
            globalMarketCoinStatsApiModel = globalMarketCoinStatsApiModel(),
            currency = Currency.GBP
        )

        assertThat(result?.totalMarketCap).isEqualTo(
            Price("2410000000000", currency = Currency.GBP)
        )
        assertThat(result?.volume24h).isEqualTo(
            Price("98200000000", currency = Currency.GBP)
        )
    }

    @Test
    fun `When global stats data is null should return null`() {
        val result = globalMarketMapper.mapApiModelsToModel(
            globalStatsApiModel = GlobalStatsApiModel(data = null),
            globalMarketCoinStatsApiModel = globalMarketCoinStatsApiModel(),
            currency = Currency.USD
        )

        assertThat(result).isNull()
    }

    @Test
    fun `When global coin stats data is null should return null`() {
        val result = globalMarketMapper.mapApiModelsToModel(
            globalStatsApiModel = globalStatsApiModel(),
            globalMarketCoinStatsApiModel = GlobalMarketCoinStatsApiModel(data = null),
            currency = Currency.USD
        )

        assertThat(result).isNull()
    }

    @Test
    fun `When required global stats fields are null should return null`() {
        listOf(
            globalStatsApiModel(totalMarketCap = null),
            globalStatsApiModel(total24hVolume = null),
            globalStatsApiModel(btcDominance = null)
        ).forEach { globalStatsApiModel ->
            val result = globalMarketMapper.mapApiModelsToModel(
                globalStatsApiModel = globalStatsApiModel,
                globalMarketCoinStatsApiModel = globalMarketCoinStatsApiModel(),
                currency = Currency.USD
            )

            assertThat(result).isNull()
        }
    }

    @Test
    fun `When required global coin stats fields are null should return null`() {
        listOf(
            globalMarketCoinStatsApiModel(positiveChanges = null),
            globalMarketCoinStatsApiModel(negativeChanges = null)
        ).forEach { globalMarketCoinStatsApiModel ->
            val result = globalMarketMapper.mapApiModelsToModel(
                globalStatsApiModel = globalStatsApiModel(),
                globalMarketCoinStatsApiModel = globalMarketCoinStatsApiModel,
                currency = Currency.USD
            )

            assertThat(result).isNull()
        }
    }

    @Test
    fun `When required numeric fields are invalid should return null`() {
        val invalidGlobalStats = listOf(
            globalStatsApiModel(totalMarketCap = "not-a-number"),
            globalStatsApiModel(total24hVolume = "not-a-number")
        )
        invalidGlobalStats.forEach { globalStatsApiModel ->
            val result = globalMarketMapper.mapApiModelsToModel(
                globalStatsApiModel = globalStatsApiModel,
                globalMarketCoinStatsApiModel = globalMarketCoinStatsApiModel(),
                currency = Currency.USD
            )

            assertThat(result).isNull()
        }
    }

    private fun globalStatsApiModel(
        totalMarketCap: String? = "2410000000000",
        total24hVolume: String? = "98200000000",
        btcDominance: Double? = 54.2,
    ) = GlobalStatsApiModel(
        data = GlobalStatsData(
            totalMarketCap = totalMarketCap,
            total24hVolume = total24hVolume,
            btcDominance = btcDominance
        )
    )

    private fun globalMarketCoinStatsApiModel(
        positiveChanges: Int? = 2841,
        negativeChanges: Int? = 1893,
    ) = GlobalMarketCoinStatsApiModel(
        data = GlobalMarketCoinStatsDataHolder(
            stats = GlobalMarketCoinStatsData(
                positiveChanges = positiveChanges,
                negativeChanges = negativeChanges
            )
        )
    )
}
