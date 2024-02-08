package dev.shorthouse.coinwatch.ui.component

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import dev.shorthouse.coinwatch.ui.theme.AppTheme
import org.junit.Rule
import org.junit.Test

class ErrorStateTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun when_displayingBaseErrorState_should_displayExpectedComponents() {
        composeTestRule.setContent {
            AppTheme {
                ErrorState(
                    message = null
                )
            }
        }

        composeTestRule.apply {
            onNodeWithContentDescription("Error").assertIsDisplayed()
            onNodeWithText("An error has occurred").assertIsDisplayed()
        }
    }

    @Test
    fun when_messageIsProvided_should_displayMessage() {
        composeTestRule.setContent {
            AppTheme {
                ErrorState(
                    message = "Error message"
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("Error message").assertIsDisplayed()
        }
    }
}
