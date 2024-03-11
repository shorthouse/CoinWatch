package dev.shorthouse.coinwatch.ui.screen

import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.BuildConfig
import dev.shorthouse.coinwatch.data.preferences.global.StartScreen
import dev.shorthouse.coinwatch.ui.screen.settings.SettingsScreen
import dev.shorthouse.coinwatch.ui.screen.settings.SettingsUiState
import dev.shorthouse.coinwatch.ui.theme.AppTheme
import org.junit.Rule
import org.junit.Test

class SettingsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun when_uiStateLoading_should_showLoadingIndicator() {
        val uiState = SettingsUiState(
            isLoading = true
        )

        composeTestRule.setContent {
            AppTheme {
                SettingsScreen(
                    uiState = uiState,
                    onNavigateUp = {},
                    onUpdateStartScreen = {}
                )
            }
        }

        composeTestRule.apply {
            onNode(SemanticsMatcher.keyIsDefined(SemanticsProperties.ProgressBarRangeInfo))
                .assertIsDisplayed()
        }
    }

    @Test
    fun when_uiStateError_should_displayErrorMessage() {
        val errorMessage = "Error loading settings screen"
        val uiState = SettingsUiState(
            errorMessage = errorMessage
        )

        composeTestRule.setContent {
            AppTheme {
                SettingsScreen(
                    uiState = uiState,
                    onNavigateUp = {},
                    onUpdateStartScreen = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText(errorMessage).assertIsDisplayed()
        }
    }

    @Test
    fun when_uiStateSuccess_should_displayExpectedContent() {
        val uiState = SettingsUiState()

        composeTestRule.setContent {
            AppTheme {
                SettingsScreen(
                    uiState = uiState,
                    onNavigateUp = {},
                    onUpdateStartScreen = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithContentDescription("Back").assertIsDisplayed()
            onNodeWithText("Settings").assertIsDisplayed()
            onNodeWithText("Preferences").assertIsDisplayed()
            onNodeWithText("Start screen").assertIsDisplayed()
            onNodeWithText("Market").assertIsDisplayed()
            onNodeWithText("About").assertIsDisplayed()
            onNodeWithText("CoinWatch version").assertIsDisplayed()
            onNodeWithText(BuildConfig.VERSION_NAME).assertIsDisplayed()
            onNodeWithText("Source code").assertIsDisplayed()
            onNodeWithText("Available on GitHub").assertIsDisplayed()
            onNodeWithText("Privacy policy").assertIsDisplayed()
            onNodeWithText("Feedback").assertIsDisplayed()
            onNodeWithText("Rate CoinWatch").assertIsDisplayed()
            onNodeWithText("Leave a Play Store review").assertIsDisplayed()
        }
    }

    @Test
    fun when_backButtonClicked_should_callOnNavigateUp() {
        val uiState = SettingsUiState()

        var onNavigateUpCalled = false

        composeTestRule.setContent {
            AppTheme {
                SettingsScreen(
                    uiState = uiState,
                    onNavigateUp = { onNavigateUpCalled = true },
                    onUpdateStartScreen = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithContentDescription("Back").performClick()
        }

        assertThat(onNavigateUpCalled).isTrue()
    }

    @Test
    fun when_startScreenClicked_should_openStartScreenDialog() {
        val uiState = SettingsUiState()

        composeTestRule.setContent {
            AppTheme {
                SettingsScreen(
                    uiState = uiState,
                    onNavigateUp = {},
                    onUpdateStartScreen = {}
                )
            }
        }

        val radioButtonMatcher = SemanticsMatcher.expectValue(
            SemanticsProperties.Role,
            Role.RadioButton
        )
        composeTestRule.apply {
            onNodeWithText("Start screen").performClick()
            onNode(hasText("Market").and(radioButtonMatcher))
                .assertIsDisplayed().assertHasClickAction()
            onNode(hasText("Favourites").and(radioButtonMatcher))
                .assertIsDisplayed().assertHasClickAction()
            onNode(hasText("Search").and(radioButtonMatcher))
                .assertIsDisplayed().assertHasClickAction()
        }
    }

    @Test
    fun when_startScreenDialogOptionClicked_should_callOnUpdateStartScreen() {
        val uiState = SettingsUiState()

        var onUpdateStartScreenCalled = false

        composeTestRule.setContent {
            AppTheme {
                SettingsScreen(
                    uiState = uiState,
                    onNavigateUp = {},
                    onUpdateStartScreen = { startScreen ->
                        onUpdateStartScreenCalled = startScreen == StartScreen.Search
                    }
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("Start screen").performClick()
            onNodeWithText("Search").performClick()
        }

        assertThat(onUpdateStartScreenCalled).isTrue()
    }

    @Test
    fun when_startScreenIsMarket_should_haveMarketOptionSelected() {
        val uiState = SettingsUiState(
            startScreen = StartScreen.Market
        )

        composeTestRule.setContent {
            AppTheme {
                SettingsScreen(
                    uiState = uiState,
                    onNavigateUp = {},
                    onUpdateStartScreen = {}
                )
            }
        }

        val radioButtonMatcher = SemanticsMatcher.expectValue(
            SemanticsProperties.Role,
            Role.RadioButton
        )
        composeTestRule.apply {
            onNodeWithText("Market").assertIsDisplayed()
            onNodeWithText("Start screen").performClick()

            onNode(hasText("Market").and(radioButtonMatcher))
                .assertIsDisplayed().assertIsSelected()
            onNode(hasText("Favourites").and(radioButtonMatcher))
                .assertIsDisplayed().assertIsNotSelected()
            onNode(hasText("Search").and(radioButtonMatcher))
                .assertIsDisplayed().assertIsNotSelected()
        }
    }

    @Test
    fun when_startScreenIsFavourites_should_haveFavouritesOptionSelected() {
        val uiState = SettingsUiState(
            startScreen = StartScreen.Favourites
        )

        composeTestRule.setContent {
            AppTheme {
                SettingsScreen(
                    uiState = uiState,
                    onNavigateUp = {},
                    onUpdateStartScreen = {}
                )
            }
        }

        val radioButtonMatcher = SemanticsMatcher.expectValue(
            SemanticsProperties.Role,
            Role.RadioButton
        )
        composeTestRule.apply {
            onNodeWithText("Favourites").assertIsDisplayed()
            onNodeWithText("Start screen").performClick()

            onNode(hasText("Market").and(radioButtonMatcher))
                .assertIsDisplayed().assertIsNotSelected()
            onNode(hasText("Favourites").and(radioButtonMatcher))
                .assertIsDisplayed().assertIsSelected()
            onNode(hasText("Search").and(radioButtonMatcher))
                .assertIsDisplayed().assertIsNotSelected()
        }
    }

    @Test
    fun when_startScreenIsSearch_should_haveSearchOptionSelected() {
        val uiState = SettingsUiState(
            startScreen = StartScreen.Search
        )

        composeTestRule.setContent {
            AppTheme {
                SettingsScreen(
                    uiState = uiState,
                    onNavigateUp = {},
                    onUpdateStartScreen = {}
                )
            }
        }

        val radioButtonMatcher = SemanticsMatcher.expectValue(
            SemanticsProperties.Role,
            Role.RadioButton
        )
        composeTestRule.apply {
            onNodeWithText("Search").assertIsDisplayed()
            onNodeWithText("Start screen").performClick()

            onNode(hasText("Market").and(radioButtonMatcher))
                .assertIsDisplayed().assertIsNotSelected()
            onNode(hasText("Favourites").and(radioButtonMatcher))
                .assertIsDisplayed().assertIsNotSelected()
            onNode(hasText("Search").and(radioButtonMatcher))
                .assertIsDisplayed().assertIsSelected()
        }
    }
}
