package dev.shorthouse.coinwatch.ui.screen

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onParent
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.R
import dev.shorthouse.coinwatch.data.preferences.global.CoinSort
import dev.shorthouse.coinwatch.data.source.local.model.Coin
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.Price
import dev.shorthouse.coinwatch.ui.model.TimeOfDay
import dev.shorthouse.coinwatch.ui.screen.market.MarketScreen
import dev.shorthouse.coinwatch.ui.screen.market.MarketUiState
import dev.shorthouse.coinwatch.ui.theme.AppTheme
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import org.junit.Rule
import org.junit.Test

class MarketScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val bitcoin = Coin(
        id = "bitcoin",
        symbol = "BTC",
        name = "Bitcoin",
        imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
        currentPrice = Price("29446.336548759988"),
        priceChangePercentage24h = Percentage("1.76833"),
    )

    @Test
    fun when_uiStateLoading_should_showLoadingIndicator() {
        val uiState = MarketUiState(
            isLoading = true
        )

        composeTestRule.setContent {
            AppTheme {
                MarketScreen(
                    uiState = uiState,
                    onCoinClick = {},
                    onNavigateSettings = {},
                    onUpdateMarketCoinSort = {},
                    onUpdateIsCoinSortSheetShown = {},
                    onRefresh = {},
                    onDismissError = {}
                )
            }
        }

        composeTestRule.apply {
            onNode(SemanticsMatcher.keyIsDefined(SemanticsProperties.ProgressBarRangeInfo))
                .assertIsDisplayed()
        }
    }

    @Test
    fun when_timeOfDayMorning_should_displayMorningGreeting() {
        val uiState = MarketUiState(
            timeOfDay = TimeOfDay.Morning
        )

        composeTestRule.setContent {
            AppTheme {
                MarketScreen(
                    uiState = uiState,
                    onCoinClick = {},
                    onNavigateSettings = {},
                    onUpdateMarketCoinSort = {},
                    onUpdateIsCoinSortSheetShown = {},
                    onRefresh = {},
                    onDismissError = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("Good morning").assertIsDisplayed()
        }
    }

    @Test
    fun when_timeOfDayAfternoon_should_displayAfternoonGreeting() {
        val uiState = MarketUiState(
            timeOfDay = TimeOfDay.Afternoon
        )

        composeTestRule.setContent {
            AppTheme {
                MarketScreen(
                    uiState = uiState,
                    onCoinClick = {},
                    onNavigateSettings = {},
                    onUpdateMarketCoinSort = {},
                    onUpdateIsCoinSortSheetShown = {},
                    onRefresh = {},
                    onDismissError = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("Good afternoon").assertIsDisplayed()
        }
    }

    @Test
    fun when_timeOfDayEvening_should_displayEveningGreeting() {
        val uiState = MarketUiState(
            timeOfDay = TimeOfDay.Evening
        )

        composeTestRule.setContent {
            AppTheme {
                MarketScreen(
                    uiState = uiState,
                    onCoinClick = {},
                    onNavigateSettings = {},
                    onUpdateMarketCoinSort = {},
                    onUpdateIsCoinSortSheetShown = {},
                    onRefresh = {},
                    onDismissError = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("Good evening").assertIsDisplayed()
        }
    }

    @Test
    fun when_marketCapChangeNull_should_notDisplayMarketText() {
        val uiState = MarketUiState(
            marketCapChangePercentage24h = null
        )

        composeTestRule.setContent {
            AppTheme {
                MarketScreen(
                    uiState = uiState,
                    onCoinClick = {},
                    onNavigateSettings = {},
                    onUpdateMarketCoinSort = {},
                    onUpdateIsCoinSortSheetShown = {},
                    onRefresh = {},
                    onDismissError = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("Market is up").assertDoesNotExist()
            onNodeWithText("Market is down").assertDoesNotExist()
            onNodeWithText("Market is flat").assertDoesNotExist()
        }
    }

    @Test
    fun when_marketCapChangePositive_should_displayMarketUpText() {
        val uiState = MarketUiState(
            marketCapChangePercentage24h = Percentage("1.2")
        )

        composeTestRule.setContent {
            AppTheme {
                MarketScreen(
                    uiState = uiState,
                    onCoinClick = {},
                    onNavigateSettings = {},
                    onUpdateMarketCoinSort = {},
                    onUpdateIsCoinSortSheetShown = {},
                    onRefresh = {},
                    onDismissError = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("Market is up").assertIsDisplayed()
            onNodeWithText("Market is down").assertDoesNotExist()
            onNodeWithText("Market is flat").assertDoesNotExist()
        }
    }

    @Test
    fun when_marketCapChangeNegative_should_displayMarketDownText() {
        val uiState = MarketUiState(
            marketCapChangePercentage24h = Percentage("-1.2")
        )

        composeTestRule.setContent {
            AppTheme {
                MarketScreen(
                    uiState = uiState,
                    onCoinClick = {},
                    onNavigateSettings = {},
                    onUpdateMarketCoinSort = {},
                    onUpdateIsCoinSortSheetShown = {},
                    onRefresh = {},
                    onDismissError = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("Market is up").assertDoesNotExist()
            onNodeWithText("Market is down").assertIsDisplayed()
            onNodeWithText("Market is flat").assertDoesNotExist()
        }
    }

    @Test
    fun when_marketCapChangeZero_should_displayMarketFlatText() {
        val uiState = MarketUiState(
            marketCapChangePercentage24h = Percentage("0.00")
        )

        composeTestRule.setContent {
            AppTheme {
                MarketScreen(
                    uiState = uiState,
                    onCoinClick = {},
                    onNavigateSettings = {},
                    onUpdateMarketCoinSort = {},
                    onUpdateIsCoinSortSheetShown = {},
                    onRefresh = {},
                    onDismissError = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("Market is up").assertDoesNotExist()
            onNodeWithText("Market is down").assertDoesNotExist()
            onNodeWithText("Market is flat").assertIsDisplayed()
        }
    }

    @Test
    fun when_noCoinsInList_should_notShowCurrencyOrSortChips() {
        val uiState = MarketUiState(
            coinSort = CoinSort.MarketCap
        )

        composeTestRule.setContent {
            AppTheme {
                MarketScreen(
                    uiState = uiState,
                    onCoinClick = {},
                    onNavigateSettings = {},
                    onUpdateMarketCoinSort = {},
                    onUpdateIsCoinSortSheetShown = {},
                    onRefresh = {},
                    onDismissError = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("USD").assertDoesNotExist()
            onNodeWithText("Market Cap").assertDoesNotExist()
        }
    }

    @Test
    fun when_coinSortMarketCap_should_displaySelectedCoinSortAsMarketCap() {
        val uiState = MarketUiState(
            coinSort = CoinSort.MarketCap,
            coins = persistentListOf(bitcoin)
        )

        composeTestRule.setContent {
            AppTheme {
                MarketScreen(
                    uiState = uiState,
                    onCoinClick = {},
                    onNavigateSettings = {},
                    onUpdateMarketCoinSort = {},
                    onUpdateIsCoinSortSheetShown = {},
                    onRefresh = {},
                    onDismissError = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("Market Cap").assertIsDisplayed()
        }
    }

    @Test
    fun when_coinSortPrice_should_displaySelectedCoinSortAsPrice() {
        val uiState = MarketUiState(
            coinSort = CoinSort.Price,
            coins = persistentListOf(bitcoin)
        )

        composeTestRule.setContent {
            AppTheme {
                MarketScreen(
                    uiState = uiState,
                    onCoinClick = {},
                    onNavigateSettings = {},
                    onUpdateMarketCoinSort = {},
                    onUpdateIsCoinSortSheetShown = {},
                    onRefresh = {},
                    onDismissError = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("Price").assertIsDisplayed()
        }
    }

    @Test
    fun when_coinSortPriceChange_should_displaySelectedCoinSortAsPriceChange() {
        val uiState = MarketUiState(
            coinSort = CoinSort.PriceChange24h,
            coins = persistentListOf(bitcoin)
        )

        composeTestRule.setContent {
            AppTheme {
                MarketScreen(
                    uiState = uiState,
                    onCoinClick = {},
                    onNavigateSettings = {},
                    onUpdateMarketCoinSort = {},
                    onUpdateIsCoinSortSheetShown = {},
                    onRefresh = {},
                    onDismissError = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("Price Change (24h)").assertIsDisplayed()
        }
    }

    @Test
    fun when_coinSortVolume_should_displaySelectedCoinSortAsVolume() {
        val uiState = MarketUiState(
            coinSort = CoinSort.Volume24h,
            coins = persistentListOf(bitcoin)
        )

        composeTestRule.setContent {
            AppTheme {
                MarketScreen(
                    uiState = uiState,
                    onCoinClick = {},
                    onNavigateSettings = {},
                    onUpdateMarketCoinSort = {},
                    onUpdateIsCoinSortSheetShown = {},
                    onRefresh = {},
                    onDismissError = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("Volume (24h)").assertIsDisplayed()
        }
    }

    @Test
    fun when_clickCoinSortChip_should_callShowCoinSortBottomSheet() {
        var showCoinSortBottomSheetCalled = false
        val uiState = MarketUiState(
            coinSort = CoinSort.MarketCap,
            coins = persistentListOf(bitcoin)
        )

        composeTestRule.setContent {
            AppTheme {
                MarketScreen(
                    uiState = uiState,
                    onCoinClick = {},
                    onNavigateSettings = {},
                    onUpdateMarketCoinSort = {},
                    onUpdateIsCoinSortSheetShown = { showSheet ->
                        showCoinSortBottomSheetCalled = showSheet
                    },
                    onRefresh = {},
                    onDismissError = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("Market Cap").performClick()
        }

        assertThat(showCoinSortBottomSheetCalled).isTrue()
    }

    @Test
    fun when_noCoins_should_showCoinsEmptyState() {
        val uiState = MarketUiState(
            coins = persistentListOf()
        )

        composeTestRule.setContent {
            AppTheme {
                MarketScreen(
                    uiState = uiState,
                    onCoinClick = {},
                    onNavigateSettings = {},
                    onUpdateMarketCoinSort = {},
                    onUpdateIsCoinSortSheetShown = {},
                    onRefresh = {},
                    onDismissError = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("Couldn't load coins").assertIsDisplayed()
            onNodeWithText("Check your internet connection").assertIsDisplayed()
        }
    }

    @Test
    fun when_coinsExist_should_displayExpectedCoinList() {
        val uiState = MarketUiState(
            coins = persistentListOf(
                Coin(
                    id = "bitcoin",
                    symbol = "BTC",
                    name = "Bitcoin",
                    imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                    currentPrice = Price("29446.336548759988"),
                    priceChangePercentage24h = Percentage("1.76833"),
                ),
                Coin(
                    id = "ethereum",
                    symbol = "ETH",
                    name = "Ethereum",
                    imageUrl = "https://cdn.coinranking.com/rk4RKHOuW/eth.svg",
                    currentPrice = Price("1875.473083380222"),
                    priceChangePercentage24h = Percentage("-1.84"),
                )
            )
        )

        composeTestRule.setContent {
            AppTheme {
                MarketScreen(
                    uiState = uiState,
                    onCoinClick = {},
                    onNavigateSettings = {},
                    onUpdateMarketCoinSort = {},
                    onUpdateIsCoinSortSheetShown = {},
                    onRefresh = {},
                    onDismissError = {}
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
    fun when_coinItemClicked_should_callOnCoinClick() {
        var onCoinClickCalled = false

        val uiStateSuccess = MarketUiState(
            coins = persistentListOf(bitcoin)
        )

        composeTestRule.setContent {
            AppTheme {
                MarketScreen(
                    uiState = uiStateSuccess,
                    onCoinClick = { onCoinClickCalled = true },
                    onNavigateSettings = {},
                    onUpdateMarketCoinSort = {},
                    onUpdateIsCoinSortSheetShown = {},
                    onRefresh = {},
                    onDismissError = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("Bitcoin").performClick()
        }

        assertThat(onCoinClickCalled).isTrue()
    }

    @Test
    fun when_showCoinSortBottomSheetTrue_should_showBottomSheet() {
        val uiState = MarketUiState(
            isCoinSortSheetShown = true,
            coinSort = CoinSort.MarketCap
        )

        composeTestRule.setContent {
            AppTheme {
                MarketScreen(
                    uiState = uiState,
                    onCoinClick = {},
                    onNavigateSettings = {},
                    onUpdateMarketCoinSort = {},
                    onUpdateIsCoinSortSheetShown = {},
                    onRefresh = {},
                    onDismissError = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("Sort coins by").assertIsDisplayed()
            onNode(hasText("Market Cap").and(isSelected())).assertIsDisplayed()
            onNodeWithText("Price").assertIsDisplayed()
            onNodeWithText("Price Change (24h)").assertIsDisplayed()
            onNodeWithText("Volume (24h)").assertIsDisplayed()
        }
    }

    @Test
    fun when_chooseCoinSortBottomSheetOption_should_callUpdateCoinSort() {
        var updateCoinSortCalled = false
        val uiState = MarketUiState(
            coinSort = CoinSort.MarketCap,
            isCoinSortSheetShown = true
        )

        composeTestRule.setContent {
            AppTheme {
                MarketScreen(
                    uiState = uiState,
                    onCoinClick = {},
                    onNavigateSettings = {},
                    onUpdateMarketCoinSort = { coinSort ->
                        updateCoinSortCalled = coinSort == CoinSort.Price
                    },
                    onUpdateIsCoinSortSheetShown = {},
                    onRefresh = {},
                    onDismissError = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("Price").performClick()
        }

        assertThat(updateCoinSortCalled).isTrue()
    }

    @Test
    fun when_scrollingCoinList_should_showScrollToTopFab() {
        val coins = (1..20).map {
            Coin(
                id = it.toString(),
                symbol = "",
                name = it.toString(),
                imageUrl = "",
                currentPrice = Price(null),
                priceChangePercentage24h = Percentage(null),
            )
        }.toPersistentList()

        val uiState = MarketUiState(
            coins = coins
        )

        composeTestRule.setContent {
            AppTheme {
                MarketScreen(
                    uiState = uiState,
                    onCoinClick = {},
                    onNavigateSettings = {},
                    onUpdateMarketCoinSort = {},
                    onUpdateIsCoinSortSheetShown = {},
                    onRefresh = {},
                    onDismissError = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithContentDescription("Scroll to top").assertDoesNotExist()
            onNodeWithText("1").onParent().performScrollToIndex(coins.size - 1)
            onNodeWithContentDescription("Scroll to top").assertIsDisplayed()
        }
    }

    @Test
    fun when_pullRefreshingScreen_should_callOnRefresh() {
        var onRefreshCalled = false

        val uiState = MarketUiState(
            coins = persistentListOf(bitcoin)
        )

        composeTestRule.setContent {
            AppTheme {
                MarketScreen(
                    uiState = uiState,
                    onCoinClick = {},
                    onNavigateSettings = {},
                    onUpdateMarketCoinSort = {},
                    onUpdateIsCoinSortSheetShown = {},
                    onRefresh = { onRefreshCalled = true },
                    onDismissError = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("Bitcoin")
                .onParent()
                .performTouchInput {
                    swipeDown()
                }
        }

        assertThat(onRefreshCalled).isTrue()
    }

    @Test
    fun when_errorMessageListNotEmpty_should_showErrorMessageSnackbar() {
        val uiState = MarketUiState(
            errorMessageIds = listOf(R.string.error_network_coins, R.string.error_local_coins)
        )

        composeTestRule.setContent {
            AppTheme {
                MarketScreen(
                    uiState = uiState,
                    onCoinClick = {},
                    onNavigateSettings = {},
                    onUpdateMarketCoinSort = {},
                    onUpdateIsCoinSortSheetShown = {},
                    onRefresh = {},
                    onDismissError = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("Latest coin data unavailable").assertIsDisplayed()
        }
    }

    @Test
    fun when_coinsListHasItems_should_displaySearchPromptAtBottomOfList() {
        val uiState = MarketUiState(
            coins = persistentListOf(bitcoin)
        )

        composeTestRule.setContent {
            AppTheme {
                MarketScreen(
                    uiState = uiState,
                    onCoinClick = {},
                    onNavigateSettings = {},
                    onUpdateMarketCoinSort = {},
                    onUpdateIsCoinSortSheetShown = {},
                    onRefresh = {},
                    onDismissError = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("Can't find the coin you're looking for?").assertIsDisplayed()
            onNodeWithText("Try using the search function!").assertIsDisplayed()
        }
    }

    @Test
    fun when_moreButtonClicked_should_toggleDropdownMenu() {
        val uiState = MarketUiState()

        composeTestRule.setContent {
            AppTheme {
                MarketScreen(
                    uiState = uiState,
                    onCoinClick = {},
                    onNavigateSettings = {},
                    onUpdateMarketCoinSort = {},
                    onUpdateIsCoinSortSheetShown = {},
                    onRefresh = {},
                    onDismissError = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithContentDescription("More").performClick()
            onNodeWithText("Settings").assertIsDisplayed().assertHasClickAction()
            onNodeWithContentDescription("More").performClick()
            onNodeWithText("Settings").assertDoesNotExist()
        }
    }

    @Test
    fun when_dropdownMenuSettingsClicked_should_callOnNavigateSettings() {
        var onNavigateSettingsCalled = false
        val uiState = MarketUiState()

        composeTestRule.setContent {
            AppTheme {
                MarketScreen(
                    uiState = uiState,
                    onCoinClick = {},
                    onNavigateSettings = { onNavigateSettingsCalled = true },
                    onUpdateMarketCoinSort = {},
                    onUpdateIsCoinSortSheetShown = {},
                    onRefresh = {},
                    onDismissError = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithContentDescription("More").performClick()
            onNodeWithText("Settings").performClick()
        }

        assertThat(onNavigateSettingsCalled).isTrue()
    }
}
