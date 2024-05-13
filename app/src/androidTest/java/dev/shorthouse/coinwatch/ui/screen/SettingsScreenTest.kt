package dev.shorthouse.coinwatch.ui.screen

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isNotSelected
import androidx.compose.ui.test.isSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.BuildConfig
import dev.shorthouse.coinwatch.data.source.local.preferences.global.Currency
import dev.shorthouse.coinwatch.data.source.local.preferences.global.StartScreen
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
                    onUpdateCurrency = {},
                    onUpdateIsCurrencySheetShown = {},
                    onUpdateStartScreen = {},
                    onUpdateIsStartScreenSheetShown = {}
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
                    onUpdateCurrency = {},
                    onUpdateIsCurrencySheetShown = {},
                    onUpdateStartScreen = {},
                    onUpdateIsStartScreenSheetShown = {}
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
                    onUpdateCurrency = {},
                    onUpdateIsCurrencySheetShown = {},
                    onUpdateStartScreen = {},
                    onUpdateIsStartScreenSheetShown = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithContentDescription("Back").assertIsDisplayed()
            onNodeWithText("Settings").assertIsDisplayed()
            onNodeWithText("Preferences").assertIsDisplayed()
            onNodeWithText("Currency").assertIsDisplayed()
            onNodeWithText("USD").assertIsDisplayed()
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
                    onUpdateCurrency = {},
                    onUpdateIsCurrencySheetShown = {},
                    onUpdateStartScreen = {},
                    onUpdateIsStartScreenSheetShown = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithContentDescription("Back").performClick()
        }

        assertThat(onNavigateUpCalled).isTrue()
    }

    @Test
    fun when_currencyIsUsd_should_displayCurrencyAsUsd() {
        val uiState = SettingsUiState(
            currency = Currency.USD
        )

        composeTestRule.setContent {
            AppTheme {
                SettingsScreen(
                    uiState = uiState,
                    onNavigateUp = {},
                    onUpdateCurrency = {},
                    onUpdateIsCurrencySheetShown = {},
                    onUpdateStartScreen = {},
                    onUpdateIsStartScreenSheetShown = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("Currency").assertIsDisplayed()
            onNodeWithText("USD").assertIsDisplayed()
        }
    }

    @Test
    fun when_currencyIsGbp_should_displayCurrencyAsGbp() {
        val uiState = SettingsUiState(
            currency = Currency.GBP
        )

        composeTestRule.setContent {
            AppTheme {
                SettingsScreen(
                    uiState = uiState,
                    onNavigateUp = {},
                    onUpdateCurrency = {},
                    onUpdateIsCurrencySheetShown = {},
                    onUpdateStartScreen = {},
                    onUpdateIsStartScreenSheetShown = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("Currency").assertIsDisplayed()
            onNodeWithText("GBP").assertIsDisplayed()
        }
    }

    @Test
    fun when_currencyIsEur_should_displayCurrencyAsEur() {
        val uiState = SettingsUiState(
            currency = Currency.EUR
        )

        composeTestRule.setContent {
            AppTheme {
                SettingsScreen(
                    uiState = uiState,
                    onNavigateUp = {},
                    onUpdateCurrency = {},
                    onUpdateIsCurrencySheetShown = {},
                    onUpdateStartScreen = {},
                    onUpdateIsStartScreenSheetShown = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("Currency").assertIsDisplayed()
            onNodeWithText("EUR").assertIsDisplayed()
        }
    }

    @Test
    fun when_currencySettingsClicked_should_callShowCurrencyBottomSheet() {
        var showCurrencyBottomSheet = false
        val uiState = SettingsUiState()

        composeTestRule.setContent {
            AppTheme {
                SettingsScreen(
                    uiState = uiState,
                    onNavigateUp = {},
                    onUpdateCurrency = {},
                    onUpdateIsCurrencySheetShown = { showCurrencyBottomSheet = true },
                    onUpdateStartScreen = {},
                    onUpdateIsStartScreenSheetShown = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Currency").performClick()
        assertThat(showCurrencyBottomSheet).isTrue()
    }

    @Test
    fun when_currencyIsUsd_should_haveUsdSelectedInCurrencyBottomSheet() {
        val uiState = SettingsUiState(
            currency = Currency.USD,
            isCurrencySheetShown = true
        )

        composeTestRule.setContent {
            AppTheme {
                SettingsScreen(
                    uiState = uiState,
                    onNavigateUp = {},
                    onUpdateCurrency = {},
                    onUpdateIsCurrencySheetShown = {},
                    onUpdateStartScreen = {},
                    onUpdateIsStartScreenSheetShown = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("Coin currency").assertIsDisplayed()
            onNode(hasText("USD").and(isSelected())).assertIsDisplayed()
            onNode(hasText("GBP").and(isNotSelected())).assertIsDisplayed()
            onNode(hasText("EUR").and(isNotSelected())).assertIsDisplayed()
        }
    }

    @Test
    fun when_currencyIsGbp_should_haveGbpSelectedInCurrencyBottomSheet() {
        val uiState = SettingsUiState(
            currency = Currency.GBP,
            isCurrencySheetShown = true
        )

        composeTestRule.setContent {
            AppTheme {
                SettingsScreen(
                    uiState = uiState,
                    onNavigateUp = {},
                    onUpdateCurrency = {},
                    onUpdateIsCurrencySheetShown = {},
                    onUpdateStartScreen = {},
                    onUpdateIsStartScreenSheetShown = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("Coin currency").assertIsDisplayed()
            onNode(hasText("USD").and(isNotSelected())).assertIsDisplayed()
            onNode(hasText("GBP").and(isSelected())).assertIsDisplayed()
            onNode(hasText("EUR").and(isNotSelected())).assertIsDisplayed()
        }
    }

    @Test
    fun when_currencyIsEur_should_haveEurSelectedInCurrencyBottomSheet() {
        val uiState = SettingsUiState(
            currency = Currency.EUR,
            isCurrencySheetShown = true
        )

        composeTestRule.setContent {
            AppTheme {
                SettingsScreen(
                    uiState = uiState,
                    onNavigateUp = {},
                    onUpdateCurrency = {},
                    onUpdateIsCurrencySheetShown = {},
                    onUpdateStartScreen = {},
                    onUpdateIsStartScreenSheetShown = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("Coin currency").assertIsDisplayed()
            onNode(hasText("USD").and(isNotSelected())).assertIsDisplayed()
            onNode(hasText("GBP").and(isNotSelected())).assertIsDisplayed()
            onNode(hasText("EUR").and(isSelected())).assertIsDisplayed()
        }
    }

    @Test
    fun when_chooseCurrencyBottomSheetOption_should_callUpdateCurrency() {
        var updateCurrencyCalled = false
        val uiState = SettingsUiState(
            currency = Currency.USD,
            isCurrencySheetShown = true
        )

        composeTestRule.setContent {
            AppTheme {
                SettingsScreen(
                    uiState = uiState,
                    onNavigateUp = {},
                    onUpdateCurrency = { currency ->
                        updateCurrencyCalled = currency == Currency.GBP
                    },
                    onUpdateIsCurrencySheetShown = {},
                    onUpdateStartScreen = {},
                    onUpdateIsStartScreenSheetShown = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("GBP").performClick()
        }

        assertThat(updateCurrencyCalled).isTrue()
    }

    @Test
    fun when_startScreenIsMarket_should_displayStartScreenAsMarket() {
        val uiState = SettingsUiState(
            startScreen = StartScreen.Market
        )

        composeTestRule.setContent {
            AppTheme {
                SettingsScreen(
                    uiState = uiState,
                    onNavigateUp = {},
                    onUpdateCurrency = {},
                    onUpdateIsCurrencySheetShown = {},
                    onUpdateStartScreen = {},
                    onUpdateIsStartScreenSheetShown = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("Start screen").assertIsDisplayed()
            onNodeWithText("Market").assertIsDisplayed()
        }
    }

    @Test
    fun when_startScreenIsFavourites_should_displayStartScreenAsFavourites() {
        val uiState = SettingsUiState(
            startScreen = StartScreen.Favourites
        )

        composeTestRule.setContent {
            AppTheme {
                SettingsScreen(
                    uiState = uiState,
                    onNavigateUp = {},
                    onUpdateCurrency = {},
                    onUpdateIsCurrencySheetShown = {},
                    onUpdateStartScreen = {},
                    onUpdateIsStartScreenSheetShown = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("Start screen").assertIsDisplayed()
            onNodeWithText("Favourites").assertIsDisplayed()
        }
    }

    @Test
    fun when_startScreenIsSearch_should_displayStartScreenAsSearch() {
        val uiState = SettingsUiState(
            startScreen = StartScreen.Search
        )

        composeTestRule.setContent {
            AppTheme {
                SettingsScreen(
                    uiState = uiState,
                    onNavigateUp = {},
                    onUpdateCurrency = {},
                    onUpdateIsCurrencySheetShown = {},
                    onUpdateStartScreen = {},
                    onUpdateIsStartScreenSheetShown = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("Start screen").assertIsDisplayed()
            onNodeWithText("Search").assertIsDisplayed()
        }
    }

    @Test
    fun when_startScreenSettingsClicked_should_callShowStartScreenBottomSheet() {
        val uiState = SettingsUiState()
        var showStartScreenBottomSheet = false

        composeTestRule.setContent {
            AppTheme {
                SettingsScreen(
                    uiState = uiState,
                    onNavigateUp = {},
                    onUpdateCurrency = {},
                    onUpdateIsCurrencySheetShown = {},
                    onUpdateStartScreen = {},
                    onUpdateIsStartScreenSheetShown = { showStartScreenBottomSheet = true }
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("Start screen").performClick()
        }

        assertThat(showStartScreenBottomSheet).isTrue()
    }

    @Test
    fun when_startScreenIsMarket_should_haveMarketSelectedInStartScreenBottomSheet() {
        val uiState = SettingsUiState(
            startScreen = StartScreen.Market,
            isStartScreenSheetShown = true
        )

        composeTestRule.setContent {
            AppTheme {
                SettingsScreen(
                    uiState = uiState,
                    onNavigateUp = {},
                    onUpdateCurrency = {},
                    onUpdateIsCurrencySheetShown = {},
                    onUpdateStartScreen = {},
                    onUpdateIsStartScreenSheetShown = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("App start screen").assertIsDisplayed()
            onNode(hasText("Market").and(isSelected())).assertIsDisplayed()
            onNode(hasText("Favourites").and(isNotSelected())).assertIsDisplayed()
            onNode(hasText("Search").and(isNotSelected())).assertIsDisplayed()
        }
    }

    @Test
    fun when_startScreenIsFavourites_should_haveFavouritesSelectedInStartScreenBottomSheet() {
        val uiState = SettingsUiState(
            startScreen = StartScreen.Favourites,
            isStartScreenSheetShown = true
        )

        composeTestRule.setContent {
            AppTheme {
                SettingsScreen(
                    uiState = uiState,
                    onNavigateUp = {},
                    onUpdateCurrency = {},
                    onUpdateIsCurrencySheetShown = {},
                    onUpdateStartScreen = {},
                    onUpdateIsStartScreenSheetShown = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("App start screen").assertIsDisplayed()
            onNode(hasText("Market").and(isNotSelected())).assertIsDisplayed()
            onNode(hasText("Favourites").and(isSelected())).assertIsDisplayed()
            onNode(hasText("Search").and(isNotSelected())).assertIsDisplayed()
        }
    }

    @Test
    fun when_startScreenIsSearch_should_haveSearchSelectedInStartScreenBottomSheet() {
        val uiState = SettingsUiState(
            startScreen = StartScreen.Search,
            isStartScreenSheetShown = true
        )

        composeTestRule.setContent {
            AppTheme {
                SettingsScreen(
                    uiState = uiState,
                    onNavigateUp = {},
                    onUpdateCurrency = {},
                    onUpdateIsCurrencySheetShown = {},
                    onUpdateStartScreen = {},
                    onUpdateIsStartScreenSheetShown = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("App start screen").assertIsDisplayed()
            onNode(hasText("Market").and(isNotSelected())).assertIsDisplayed()
            onNode(hasText("Favourites").and(isNotSelected())).assertIsDisplayed()
            onNode(hasText("Search").and(isSelected())).assertIsDisplayed()
        }
    }

    @Test
    fun when_chooseStartScreenBottomSheetOption_should_callUpdateStartScreen() {
        var updateStartScreenCalled = false
        val uiState = SettingsUiState(
            startScreen = StartScreen.Market,
            isStartScreenSheetShown = true,
        )

        composeTestRule.setContent {
            AppTheme {
                SettingsScreen(
                    uiState = uiState,
                    onNavigateUp = {},
                    onUpdateCurrency = {},
                    onUpdateIsCurrencySheetShown = {},
                    onUpdateStartScreen = { startScreen ->
                        updateStartScreenCalled = startScreen == StartScreen.Favourites
                    },
                    onUpdateIsStartScreenSheetShown = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("Favourites").performClick()
        }

        assertThat(updateStartScreenCalled).isTrue()
    }
}
