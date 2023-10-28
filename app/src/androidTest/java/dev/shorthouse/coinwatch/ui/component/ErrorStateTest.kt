package dev.shorthouse.coinwatch.ui.component

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.google.common.truth.Truth.assertThat
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
                    message = null,
                    onRetry = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithContentDescription("Error").assertIsDisplayed()
            onNodeWithText("An error has occurred").assertIsDisplayed()
            onNodeWithText("Retry").assertIsDisplayed().assertHasClickAction()
        }
    }

    @Test
    fun when_messageIsProvided_should_displayMessage() {
        composeTestRule.setContent {
            AppTheme {
                ErrorState(
                    message = "Error message",
                    onRetry = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("Error message").assertIsDisplayed()
        }
    }

    @Test
    fun when_retryClicked_should_callOnRetry() {
        var onRetryCalled = false

        composeTestRule.setContent {
            AppTheme {
                ErrorState(
                    message = null,
                    onRetry = { onRetryCalled = true }
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("Retry").performClick()
            assertThat(onRetryCalled).isTrue()
        }
    }
}
