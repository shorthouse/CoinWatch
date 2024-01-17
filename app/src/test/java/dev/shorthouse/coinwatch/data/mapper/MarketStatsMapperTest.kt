package dev.shorthouse.coinwatch.data.mapper

import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.data.source.remote.model.MarketStatsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.MarketStatsData
import dev.shorthouse.coinwatch.data.source.remote.model.MarketStatsDataHolder
import dev.shorthouse.coinwatch.model.MarketStats
import dev.shorthouse.coinwatch.model.Percentage
import org.junit.Test

class MarketStatsMapperTest {

    // Class under test
    private val marketStatsMapper = MarketStatsMapper()

    @Test
    fun `When market stats data holder is null should return default values`() {
        // Arrange
        val apiModel = MarketStatsApiModel(
            marketStatsDataHolder = null
        )

        val expectedMarketStats = MarketStats(
            marketCapChangePercentage24h = Percentage(null)
        )

        // Act
        val marketStats = marketStatsMapper.mapApiModelToModel(
            apiModel = apiModel
        )

        // Assert
        assertThat(marketStats).isEqualTo(expectedMarketStats)
    }

    @Test
    fun `When market stats data is null should return default values`() {
        // Arrange
        val apiModel = MarketStatsApiModel(
            marketStatsDataHolder = MarketStatsDataHolder(
                marketStatsData = null
            )
        )

        val expectedMarketStats = MarketStats(
            marketCapChangePercentage24h = Percentage(null)
        )

        // Act
        val marketStats = marketStatsMapper.mapApiModelToModel(
            apiModel = apiModel
        )

        // Assert
        assertThat(marketStats).isEqualTo(expectedMarketStats)
    }

    @Test
    fun `When market stats has null values should replace these with default values`() {
        // Arrange
        val apiModel = MarketStatsApiModel(
            marketStatsDataHolder = MarketStatsDataHolder(
                marketStatsData = MarketStatsData(
                    marketCapChangePercentage24h = null
                )
            )
        )

        val expectedMarketStats = MarketStats(
            marketCapChangePercentage24h = Percentage(null)
        )

        // Act
        val marketStats = marketStatsMapper.mapApiModelToModel(
            apiModel = apiModel
        )

        // Assert
        assertThat(marketStats).isEqualTo(expectedMarketStats)
    }

    @Test
    fun `When market stats has valid values should map these values correctly`() {
        // Arrange
        val apiModel = MarketStatsApiModel(
            marketStatsDataHolder = MarketStatsDataHolder(
                marketStatsData = MarketStatsData(
                    marketCapChangePercentage24h = "-0.23142324"
                )
            )
        )

        val expectedMarketStats = MarketStats(
            marketCapChangePercentage24h = Percentage("-0.23142324")
        )

        // Act
        val marketStats = marketStatsMapper.mapApiModelToModel(
            apiModel = apiModel
        )

        // Assert
        assertThat(marketStats).isEqualTo(expectedMarketStats)
    }
}
