package dev.shorthouse.coinwatch.ui.screen.pulse.component

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.model.FearGreed
import dev.shorthouse.coinwatch.model.FearGreedMoodBand
import dev.shorthouse.coinwatch.ui.theme.AppTheme
import java.math.BigDecimal
import kotlinx.collections.immutable.persistentListOf
import org.junit.Rule
import org.junit.Test

class MarketMoodCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun when_fearGreedProvided_should_displayExpectedContent() {
        composeTestRule.setContent {
            AppTheme {
                MarketMoodCard(fearGreed = fearGreed(value = 42))
            }
        }

        composeTestRule.apply {
            onNodeWithText("42").assertIsDisplayed()
            onNodeWithText("Fear").assertIsDisplayed()
            onNodeWithText("out of 100").assertIsDisplayed()
            onNodeWithText("Extreme Fear").assertIsDisplayed()
            onNodeWithText("Neutral").assertIsDisplayed()
            onNodeWithText("Extreme Greed").assertIsDisplayed()
            onNodeWithText("Past 30 days").assertIsDisplayed()
        }
    }

    @Test
    fun when_moodBandProvided_should_displayExpectedMoodLabel() {
        var moodBand by mutableStateOf(FearGreedMoodBand.ExtremeFear)

        composeTestRule.setContent {
            AppTheme {
                MarketMoodCard(fearGreed = fearGreed(moodBand = moodBand))
            }
        }

        mapOf(
            FearGreedMoodBand.ExtremeFear to "Extreme Fear",
            FearGreedMoodBand.Fear to "Fear",
            FearGreedMoodBand.Neutral to "Neutral",
            FearGreedMoodBand.Greed to "Greed",
            FearGreedMoodBand.ExtremeGreed to "Extreme Greed"
        ).forEach { (band, label) ->
            composeTestRule.runOnIdle {
                moodBand = band
            }
            composeTestRule.waitForIdle()

            assertThat(
                composeTestRule.onAllNodesWithText(label)
                    .fetchSemanticsNodes()
                    .isNotEmpty()
            ).isTrue()
        }
    }

    private fun fearGreed(
        value: Int = 42,
        moodBand: FearGreedMoodBand = FearGreedMoodBand.Fear
    ): FearGreed =
        FearGreed(
            value = value,
            moodBand = moodBand,
            history = persistentListOf(
                BigDecimal("35"),
                BigDecimal("40"),
                BigDecimal(value.toString())
            )
        )
}
