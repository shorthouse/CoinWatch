package dev.shorthouse.coinwatch.ui.component

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.ui.theme.AppTheme
import org.junit.Rule
import org.junit.Test

class PercentageChipTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun when_percentageIsPositive_should_displayPositivePercentage() {
        composeTestRule.setContent {
            AppTheme {
                PercentageChangeChip(
                    percentage = Percentage("1.23343234850923")
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("+1.23%").assertIsDisplayed()
        }
    }

    @Test
    fun when_percentageIsNegative_should_displayNegativePercentage() {
        composeTestRule.setContent {
            AppTheme {
                PercentageChangeChip(
                    percentage = Percentage("-1.23343234850923")
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("-1.23%").assertIsDisplayed()
        }
    }

    @Test
    fun when_percentageIsZero_should_displayZeroPercentage() {
        composeTestRule.setContent {
            AppTheme {
                PercentageChangeChip(
                    percentage = Percentage("0")
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("+0.00%").assertIsDisplayed()
        }
    }

    @Test
    fun when_percentageIsNull_should_displayEmptyPercentage() {
        composeTestRule.setContent {
            AppTheme {
                PercentageChangeChip(
                    percentage = Percentage(null)
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("-- %").assertIsDisplayed()
        }
    }
}
