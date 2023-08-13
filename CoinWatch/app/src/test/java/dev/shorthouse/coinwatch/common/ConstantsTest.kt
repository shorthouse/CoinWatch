package dev.shorthouse.coinwatch.common

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ConstantsTest {
    @Test
    fun `BASE_URL is expected value`() {
        // Arrange
        val expectedBaseUrl = "https://api.coinranking.com/v2/"

        // Act
        val baseUrl = Constants.BASE_URL

        // Assert
        assertThat(baseUrl).isEqualTo(expectedBaseUrl)
    }

    @Test
    fun `PARAM_COIN_ID is expected value`() {
        // Arrange
        val expectedParamCoinId = "coinId"

        // Act
        val paramCoinId = Constants.PARAM_COIN_ID

        // Assert
        assertThat(paramCoinId).isEqualTo(expectedParamCoinId)
    }

    @Test
    fun `COIN_DATABASE_NAME is expected value`() {
        // Arrange
        val expectedCoinDatabaseName = "Coin.db"

        // Act
        val coinDatabaseName = Constants.COIN_DATABASE_NAME

        // Assert
        assertThat(coinDatabaseName).isEqualTo(expectedCoinDatabaseName)
    }
}
