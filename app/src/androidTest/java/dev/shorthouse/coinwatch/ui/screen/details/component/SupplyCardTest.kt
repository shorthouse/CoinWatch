package dev.shorthouse.coinwatch.ui.screen.details.component

import androidx.compose.foundation.layout.width
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.getUnclippedBoundsInRoot
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.unit.dp
import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.common.Constants.MISSING_VALUE_PLACEHOLDER
import dev.shorthouse.coinwatch.ui.theme.AppTheme
import org.junit.Rule
import org.junit.Test

class SupplyCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun when_coinDetailsProvided_should_displayExpectedSupplyValues() {
        composeTestRule.setContent {
            AppTheme {
                SupplyCard(
                    circulatingSupply = "120,186,525",
                    totalSupply = "120,500,000",
                    maxSupply = "210,000,000"
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("Circulating Supply").assertIsDisplayed()
            onNodeWithText("120,186,525").assertIsDisplayed()
            onNodeWithText("Total Supply").assertIsDisplayed()
            onNodeWithText("120,500,000").assertIsDisplayed()
            onNodeWithText("Max Supply").assertIsDisplayed()
            onNodeWithText("210,000,000").assertIsDisplayed()
        }
    }

    @Test
    fun when_supplyValuesAreUnavailable_should_displayLabelsAndUnavailablePlaceholders() {
        composeTestRule.setContent {
            AppTheme {
                SupplyCard(
                    circulatingSupply = MISSING_VALUE_PLACEHOLDER,
                    totalSupply = MISSING_VALUE_PLACEHOLDER,
                    maxSupply = MISSING_VALUE_PLACEHOLDER
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("Circulating Supply").assertIsDisplayed()
            onNodeWithText("Total Supply").assertIsDisplayed()
            onNodeWithText("Max Supply").assertIsDisplayed()
            onAllNodesWithText(MISSING_VALUE_PLACEHOLDER).assertCountEquals(3)
        }
    }

    @Test
    fun when_valueTextIsLongAndWraps_should_displayFullValueAndLabels() {
        val longTotalSupply = "123,456,789,012,345,678,901,234,567"

        composeTestRule.setContent {
            AppTheme {
                SupplyCard(
                    circulatingSupply = "120,186,525",
                    totalSupply = longTotalSupply,
                    maxSupply = "210,000,000",
                    modifier = Modifier.width(220.dp)
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("Total Supply").assertIsDisplayed()
            onNodeWithText(longTotalSupply).assertIsDisplayed()
            onNodeWithText("Max Supply").assertIsDisplayed()
        }

        val longValueHeight = composeTestRule.onNodeWithText(longTotalSupply)
            .getUnclippedBoundsInRoot()
            .let { bounds -> bounds.bottom - bounds.top }

        assertThat(longValueHeight.value).isGreaterThan(24f)
    }

    @Test
    fun when_supplyCardDisplayed_should_showRowsInExpectedOrder() {
        composeTestRule.setContent {
            AppTheme {
                SupplyCard(
                    circulatingSupply = "120,186,525",
                    totalSupply = "120,500,000",
                    maxSupply = "210,000,000"
                )
            }
        }

        val labels = listOf(
            "Circulating Supply",
            "Total Supply",
            "Max Supply"
        )
        val labelTops = labels.map { label ->
            composeTestRule.onNodeWithText(label)
                .getUnclippedBoundsInRoot()
                .top
        }

        assertThat(labelTops).isEqualTo(labelTops.sorted())
    }
}
