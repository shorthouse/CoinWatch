package dev.shorthouse.coinwatch.ui.screen

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.model.Coin
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.Price
import dev.shorthouse.coinwatch.ui.screen.favourites.FavouriteScreen
import dev.shorthouse.coinwatch.ui.screen.favourites.FavouritesUiState
import dev.shorthouse.coinwatch.ui.theme.AppTheme
import java.math.BigDecimal
import kotlinx.collections.immutable.persistentListOf
import org.junit.Rule
import org.junit.Test

class FavouritesScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun when_uiStateLoading_should_showSkeletonLoader() {
        val uiState = FavouritesUiState(
            isLoading = true
        )

        composeTestRule.setContent {
            AppTheme {
                FavouriteScreen(
                    uiState = uiState,
                    onCoinClick = {},
                    onRefresh = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithTag("Loading").assertIsDisplayed()
        }
    }

    @Test
    fun when_uiStateError_should_showErrorState() {
        val uiState = FavouritesUiState(
            errorMessage = "Error message"
        )

        composeTestRule.setContent {
            AppTheme {
                FavouriteScreen(
                    uiState = uiState,
                    onCoinClick = {},
                    onRefresh = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("An error has occurred").assertIsDisplayed()
            onNodeWithText("Unable to fetch favourite coins").assertIsDisplayed()
            onNodeWithText("Retry").assertIsDisplayed()
            onNodeWithText("Retry").assertHasClickAction()
        }
    }

    @Test
    fun when_uiStateErrorRetryClicked_should_callOnRefresh() {
        var onRefreshCalled = false
        val uiState = FavouritesUiState(
            errorMessage = "Error message"
        )

        composeTestRule.setContent {
            AppTheme {
                FavouriteScreen(
                    uiState = uiState,
                    onCoinClick = {},
                    onRefresh = { onRefreshCalled = true }
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("Retry").performClick()
        }

        assertThat(onRefreshCalled).isTrue()
    }

    @Test
    fun when_uiStateSuccess_should_showExpectedContent() {
        val uiStateSuccess = FavouritesUiState()

        composeTestRule.setContent {
            AppTheme {
                FavouriteScreen(
                    uiState = uiStateSuccess,
                    onCoinClick = {},
                    onRefresh = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("Favourites").assertIsDisplayed()
        }
    }

    @Test
    fun when_noFavouriteCoins_should_displayEmptyState() {
        val uiStateSuccess = FavouritesUiState(
            favouriteCoins = persistentListOf()
        )

        composeTestRule.setContent {
            AppTheme {
                FavouriteScreen(
                    uiState = uiStateSuccess,
                    onCoinClick = {},
                    onRefresh = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("No favourite coins").assertIsDisplayed()
            onNodeWithText("Tap the").assertIsDisplayed()
            onNodeWithContentDescription("Favourite").assertIsDisplayed()
            onNodeWithText("to favourite a coin").assertIsDisplayed()
        }
    }

    @Test
    fun when_favouriteCoinsExist_should_displayFavouriteCoins() {
        val uiStateSuccess = FavouritesUiState(
            favouriteCoins = persistentListOf(
                Coin(
                    id = "bitcoin",
                    symbol = "BTC",
                    name = "Bitcoin",
                    imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                    currentPrice = Price("29446.336548759988"),
                    priceChangePercentage24h = Percentage("1.76833"),
                    prices24h = persistentListOf(
                        BigDecimal("29390.15178296929"),
                        BigDecimal("29428.222505493162"),
                        BigDecimal("29475.12359313808"),
                        BigDecimal("29471.20179209623")
                    )
                ),
                Coin(
                    id = "ethereum",
                    symbol = "ETH",
                    name = "Ethereum",
                    imageUrl = "https://cdn.coinranking.com/rk4RKHOuW/eth.svg",
                    currentPrice = Price("1875.473083380222"),
                    priceChangePercentage24h = Percentage("-1.84"),
                    prices24h = persistentListOf(
                        BigDecimal("1872.5227299255032"),
                        BigDecimal("1874.813847463032"),
                        BigDecimal("1877.1265051203513"),
                        BigDecimal("1879.89804628163")
                    )
                )
            )
        )

        composeTestRule.setContent {
            AppTheme {
                FavouriteScreen(
                    uiState = uiStateSuccess,
                    onCoinClick = {},
                    onRefresh = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("Bitcoin").assertIsDisplayed()
            onNodeWithText("BTC").assertIsDisplayed()
            onNodeWithText("$29,446.34").assertIsDisplayed()
            onNodeWithText("+1.77%").assertIsDisplayed()

            onNodeWithText("Ethereum").assertIsDisplayed()
            onNodeWithText("ETH").assertIsDisplayed()
            onNodeWithText("$1,875.47").assertIsDisplayed()
            onNodeWithText("-1.84%").assertIsDisplayed()
        }
    }

    @Test
    fun when_clickingCoinItem_should_callOnCoinClick() {
        var onCoinClickCalled = false

        val uiStateSuccess = FavouritesUiState(
            favouriteCoins = persistentListOf(
                Coin(
                    id = "bitcoin",
                    symbol = "BTC",
                    name = "Bitcoin",
                    imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                    currentPrice = Price("29446.336548759988"),
                    priceChangePercentage24h = Percentage("1.76833"),
                    prices24h = persistentListOf(
                        BigDecimal("29390.15178296929"),
                        BigDecimal("29428.222505493162"),
                        BigDecimal("29475.12359313808"),
                        BigDecimal("29471.20179209623")
                    )
                )
            )
        )

        composeTestRule.setContent {
            AppTheme {
                FavouriteScreen(
                    uiState = uiStateSuccess,
                    onCoinClick = { onCoinClickCalled = true },
                    onRefresh = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("Bitcoin").performClick()
        }

        assertThat(onCoinClickCalled).isTrue()
    }
}
