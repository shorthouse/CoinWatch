package dev.shorthouse.coinwatch.ui.component

import androidx.compose.material3.Text
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import dev.shorthouse.coinwatch.R
import dev.shorthouse.coinwatch.ui.theme.AppTheme
import org.junit.Rule
import org.junit.Test

class EmptyStateTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun when_displayingBaseEmptyState_should_displayExpectedContent() {
        composeTestRule.setContent {
            AppTheme {
                EmptyState(
                    image = painterResource(R.drawable.empty_state_coins),
                    title = "Empty state",
                    subtitle = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("Empty state").assertIsDisplayed()
        }
    }

    @Test
    fun when_subtitleIsProvided_should_displaySubtitle() {
        composeTestRule.setContent {
            AppTheme {
                EmptyState(
                    image = painterResource(R.drawable.empty_state_coins),
                    title = "Empty state",
                    subtitle = {
                        Text("Empty state subtitle")
                    }
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("Empty state").assertIsDisplayed()
            onNodeWithText("Empty state subtitle").assertIsDisplayed()
        }
    }
}
