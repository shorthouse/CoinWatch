// package dev.shorthouse.coinwatch.ui.screen
//
// import androidx.compose.ui.test.assertHasClickAction
// import androidx.compose.ui.test.assertIsDisplayed
// import androidx.compose.ui.test.junit4.createComposeRule
// import androidx.compose.ui.test.onNodeWithContentDescription
// import androidx.compose.ui.test.onNodeWithTag
// import androidx.compose.ui.test.onNodeWithText
// import androidx.compose.ui.test.performClick
// import com.google.common.truth.Truth.assertThat
// import dev.shorthouse.coinwatch.model.Coin
// import dev.shorthouse.coinwatch.model.Percentage
// import dev.shorthouse.coinwatch.model.Price
// import dev.shorthouse.coinwatch.ui.screen.market.MarketScreen
// import dev.shorthouse.coinwatch.ui.screen.market.MarketUiState
// import dev.shorthouse.coinwatch.ui.theme.AppTheme
// import java.math.BigDecimal
// import kotlinx.collections.immutable.persistentListOf
// import org.junit.Rule
// import org.junit.Test
//
// class CoinListScreenTest {
//
//    @get:Rule
//    val composeTestRule = createComposeRule()
//
//    @Test
//    fun when_uiStateLoading_should_showSkeletonLoader() {
//        val uiStateLoading = MarketUiState.Loading
//
//        composeTestRule.setContent {
//            AppTheme {
//                MarketScreen(
//                    uiState = uiStateLoading,
//                    onCoinClick = {},
//                    updateShowCoinSortOrderBottomSheet = { showSheet ->
//                    },
//                    onRefresh = {},
//                    onUpdateCoinSort = { coinSort ->
//                    }
//                )
//            }
//        }
//
//        composeTestRule.apply {
//            onNodeWithText("Favourites").assertIsDisplayed()
//            onNodeWithText("Coins").assertIsDisplayed()
//        }
//    }
//
//    @Test
//    fun when_uiStateError_should_showErrorState() {
//        val uiStateError = MarketUiState.Error("Error message")
//
//        composeTestRule.setContent {
//            AppTheme {
//                MarketScreen(
//                    uiState = uiStateError,
//                    onCoinClick = {},
//                    updateShowCoinSortOrderBottomSheet = { showSheet ->
//                    },
//                    onRefresh = {},
//                    onUpdateCoinSort = { coinSort ->
//                    }
//                )
//            }
//        }
//
//        composeTestRule.apply {
//            onNodeWithText("An error has occurred").assertIsDisplayed()
//            onNodeWithText("Error message").assertIsDisplayed()
//            onNodeWithText("Retry").assertIsDisplayed()
//            onNodeWithText("Retry").assertHasClickAction()
//        }
//    }
//
//    @Test
//    fun when_uiStateErrorRetryClicked_should_callOnRefresh() {
//        var onRefreshCalled = false
//        val uiStateError = MarketUiState.Error("Error message")
//
//        composeTestRule.setContent {
//            AppTheme {
//                MarketScreen(
//                    uiState = uiStateError,
//                    onCoinClick = {},
//                    updateShowCoinSortOrderBottomSheet = { showSheet ->
//                    },
//                    onRefresh = { onRefreshCalled = true },
//                    onUpdateCoinSort = { coinSort ->
//                    }
//                )
//            }
//        }
//
//        composeTestRule.apply {
//            onNodeWithText("Retry").performClick()
//        }
//
//        assertThat(onRefreshCalled).isTrue()
//    }
//
//    @Test
//    fun when_uiStateSuccess_should_showExpectedContent() {
//        val uiStateSuccess = MarketUiState.Success(
//            coins = persistentListOf()
//        )
//
//        composeTestRule.setContent {
//            AppTheme {
//                MarketScreen(
//                    uiState = uiStateSuccess,
//                    onCoinClick = {},
//                    onRefresh = {},
//                    onUpdateCoinSortOrder = { coinSortOrder ->
//                    }
//                )
//            }
//        }
//
//        composeTestRule.apply {
//            onNodeWithText("Favourites").assertIsDisplayed()
//            onNodeWithText("Coins").assertIsDisplayed()
//        }
//    }
//
//    @Test
//    fun when_uiStateSuccess_favouriteCoinsEmpty_should_showEmptyState() {
//        val uiStateSuccess = MarketUiState.Success(
//            coins = persistentListOf()
//        )
//
//        composeTestRule.setContent {
//            AppTheme {
//                MarketScreen(
//                    uiState = uiStateSuccess,
//                    onCoinClick = {},
//                    onRefresh = {},
//                    onUpdateCoinSortOrder = { coinSortOrder ->
//                        viewModel.updateCoinSortOrder(coinSortOrder)
//                    }
//                )
//            }
//        }
//
//        composeTestRule.apply {
//            onNodeWithText("No favourite coins").assertIsDisplayed()
//            onNodeWithText("Tap the").assertIsDisplayed()
//            onNodeWithContentDescription("Favourite").assertIsDisplayed()
//            onNodeWithText("to favourite a coin").assertIsDisplayed()
//        }
//    }
//
//    @Test
//    fun when_uiStateSuccess_favouriteCoinsList_should_showExpectedContent() {
//        val uiStateSuccess = MarketUiState.Success(
//            coins = persistentListOf()
//        )
//
//        composeTestRule.setContent {
//            AppTheme {
//                MarketScreen(
//                    uiState = uiStateSuccess,
//                    onCoinClick = {},
//                    onRefresh = {},
//                    onUpdateCoinSortOrder = { coinSortOrder ->
//                        viewModel.updateCoinSortOrder(coinSortOrder)
//                    }
//                )
//            }
//        }
//
//        composeTestRule.apply {
//            onNodeWithText("Bitcoin").assertIsDisplayed()
//            onNodeWithText("BTC").assertIsDisplayed()
//            onNodeWithText("$29,446.34").assertIsDisplayed()
//            onNodeWithText("+0.77%").assertIsDisplayed()
//            onNodeWithTag(testTag = "priceGraph BTC", useUnmergedTree = true).assertIsDisplayed()
//            onNodeWithText("Bitcoin").assertHasClickAction()
//
//            onNodeWithText("Ethereum").assertIsDisplayed()
//            onNodeWithText("ETH").assertIsDisplayed()
//            onNodeWithText("$1,875.47").assertIsDisplayed()
//            onNodeWithText("-1.11%").assertIsDisplayed()
//            onNodeWithTag(testTag = "priceGraph ETH", useUnmergedTree = true).assertIsDisplayed()
//            onNodeWithText("Ethereum").assertHasClickAction()
//
//            onNodeWithText("Tether").assertIsDisplayed()
//            onNodeWithText("USDT").assertIsDisplayed()
//            onNodeWithText("$1.000000").assertIsDisplayed()
//            onNodeWithText("+0.00%").assertIsDisplayed()
//            onNodeWithTag(testTag = "priceGraph USDT", useUnmergedTree = true).assertIsDisplayed()
//            onNodeWithText("Tether").assertHasClickAction()
//        }
//    }
//
//    @Test
//    fun when_uiStateSuccess_coinsEmpty_should_showEmptyState() {
//        val uiStateSuccess = MarketUiState.Success(
//            coins = persistentListOf()
//        )
//
//        composeTestRule.setContent {
//            AppTheme {
//                MarketScreen(
//                    uiState = uiStateSuccess,
//                    onCoinClick = {},
//                    onRefresh = {},
//                    onUpdateCoinSortOrder = { coinSortOrder ->
//                        viewModel.updateCoinSortOrder(coinSortOrder)
//                    }
//                )
//            }
//        }
//
//        composeTestRule.apply {
//            onNodeWithText("No coins").assertIsDisplayed()
//            onNodeWithText("Please try again later").assertIsDisplayed()
//        }
//    }
//
//    @Test
//    fun when_uiStateSuccess_coinsList_should_showExpectedContent() {
//        val uiStateSuccess = MarketUiState.Success(
//            coins = persistentListOf(
//                Coin(
//                    id = "bitcoin",
//                    symbol = "BTC",
//                    name = "Bitcoin",
//                    imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
//                    currentPrice = Price("29446.336548759988"),
//                    priceChangePercentage24h = Percentage("0.76833"),
//                    prices24h = persistentListOf(
//                        BigDecimal("29390.15178296929"),
//                        BigDecimal("29428.222505493162"),
//                        BigDecimal("29475.12359313808"),
//                        BigDecimal("29471.20179209623")
//                    )
//                ),
//                Coin(
//                    id = "ethereum",
//                    symbol = "ETH",
//                    name = "Ethereum",
//                    imageUrl = "https://cdn.coinranking.com/rk4RKHOuW/eth.svg",
//                    currentPrice = Price("1875.473083380222"),
//                    priceChangePercentage24h = Percentage("-1.11008"),
//                    prices24h = persistentListOf(
//                        BigDecimal("1854.8824120105778"),
//                        BigDecimal("1853.3272421902477"),
//                        BigDecimal("1857.8290158859397"),
//                        BigDecimal("1859.4549720388395")
//                    )
//                ),
//                Coin(
//                    id = "tether",
//                    symbol = "USDT",
//                    name = "Tether",
//                    imageUrl = "https://cdn.coinranking.com/mgHqwlCLj/usdt.svg",
//                    currentPrice = Price("1.00"),
//                    priceChangePercentage24h = Percentage("0.00"),
//                    prices24h = persistentListOf(
//                        BigDecimal("1.00"),
//                        BigDecimal("1.00"),
//                        BigDecimal("1.00"),
//                        BigDecimal("1.00")
//                    )
//                )
//            )
//        )
//
//        composeTestRule.setContent {
//            AppTheme {
//                MarketScreen(
//                    uiState = uiStateSuccess,
//                    onCoinClick = {},
//                    onRefresh = {},
//                    onUpdateCoinSortOrder = { coinSortOrder ->
//                        viewModel.updateCoinSortOrder(coinSortOrder)
//                    }
//                )
//            }
//        }
//
//        composeTestRule.apply {
//            onNodeWithText("Bitcoin").assertIsDisplayed()
//            onNodeWithText("BTC").assertIsDisplayed()
//            onNodeWithText("$29,446.34").assertIsDisplayed()
//            onNodeWithText("+0.77%").assertIsDisplayed()
//            onNodeWithText("Bitcoin").assertHasClickAction()
//
//            onNodeWithText("Ethereum").assertIsDisplayed()
//            onNodeWithText("ETH").assertIsDisplayed()
//            onNodeWithText("$1,875.47").assertIsDisplayed()
//            onNodeWithText("-1.11%").assertIsDisplayed()
//            onNodeWithText("Ethereum").assertHasClickAction()
//
//            onNodeWithText("Tether").assertIsDisplayed()
//            onNodeWithText("USDT").assertIsDisplayed()
//            onNodeWithText("$1.000000").assertIsDisplayed()
//            onNodeWithText("+0.00%").assertIsDisplayed()
//            onNodeWithText("Tether").assertHasClickAction()
//        }
//    }
//
//    @Test
//    fun when_coinItemClicked_should_callOnClick() {
//        var onCoinClickCalled = false
//
//        val uiStateSuccess = MarketUiState.Success(
//            coins = persistentListOf(
//                Coin(
//                    id = "bitcoin",
//                    symbol = "BTC",
//                    name = "Bitcoin",
//                    imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
//                    currentPrice = Price("29446.336548759988"),
//                    priceChangePercentage24h = Percentage("0.76833"),
//                    prices24h = persistentListOf(
//                        BigDecimal("29390.15178296929"),
//                        BigDecimal("29428.222505493162"),
//                        BigDecimal("29475.12359313808"),
//                        BigDecimal("29471.20179209623")
//                    )
//                )
//            )
//        )
//
//        composeTestRule.setContent {
//            AppTheme {
//                MarketScreen(
//                    uiState = uiStateSuccess,
//                    onCoinClick = ({ onCoinClickCalled = true }),
//                    onRefresh = {},
//                    onUpdateCoinSortOrder = { coinSortOrder ->
//                        viewModel.updateCoinSortOrder(coinSortOrder)
//                    }
//                )
//            }
//        }
//
//        composeTestRule.apply {
//            onNodeWithText("Bitcoin").performClick()
//        }
//
//        assertThat(onCoinClickCalled).isTrue()
//    }
//
//    @Test
//    fun when_favouriteCoinItemClicked_should_callOnClick() {
//        var onCoinClickCalled = false
//
//        val uiStateSuccess = MarketUiState.Success(
//            coins = persistentListOf()
//        )
//
//        composeTestRule.setContent {
//            AppTheme {
//                MarketScreen(
//                    uiState = uiStateSuccess,
//                    onCoinClick = ({ onCoinClickCalled = true }),
//                    onRefresh = {},
//                    onUpdateCoinSortOrder = { coinSortOrder ->
//                        viewModel.updateCoinSortOrder(coinSortOrder)
//                    }
//                )
//            }
//        }
//
//        composeTestRule.apply {
//            onNodeWithText("Bitcoin").performClick()
//        }
//
//        assertThat(onCoinClickCalled).isTrue()
//    }
//
//    @Test
//    fun when_timeOfDayMorning_should_showMorningGreeting() {
//        val uiStateSuccess = MarketUiState.Success(
//            coins = persistentListOf()
//        )
//
//        composeTestRule.setContent {
//            AppTheme {
//                MarketScreen(
//                    uiState = uiStateSuccess,
//                    onCoinClick = {},
//                    onRefresh = {},
//                    onUpdateCoinSortOrder = { coinSortOrder ->
//                        viewModel.updateCoinSortOrder(coinSortOrder)
//                    }
//                )
//            }
//        }
//
//        composeTestRule.apply {
//            onNodeWithText("Good morning").assertIsDisplayed()
//        }
//    }
//
//    @Test
//    fun when_timeOfDayAfternoon_should_showAfternoonGreeting() {
//        val uiStateSuccess = MarketUiState.Success(
//            coins = persistentListOf()
//        )
//
//        composeTestRule.setContent {
//            AppTheme {
//                MarketScreen(
//                    uiState = uiStateSuccess,
//                    onCoinClick = {},
//                    onRefresh = {},
//                    onUpdateCoinSortOrder = { coinSortOrder ->
//                        viewModel.updateCoinSortOrder(coinSortOrder)
//                    }
//                )
//            }
//        }
//
//        composeTestRule.apply {
//            onNodeWithText("Good afternoon").assertIsDisplayed()
//        }
//    }
//
//    @Test
//    fun when_timeOfDayEvening_should_showEveningGreeting() {
//        val uiStateSuccess = MarketUiState.Success(
//            coins = persistentListOf()
//        )
//
//        composeTestRule.setContent {
//            AppTheme {
//                MarketScreen(
//                    uiState = uiStateSuccess,
//                    onCoinClick = {},
//                    onRefresh = {},
//                    onUpdateCoinSortOrder = { coinSortOrder ->
//                        viewModel.updateCoinSortOrder(coinSortOrder)
//                    }
//                )
//            }
//        }
//
//        composeTestRule.apply {
//            onNodeWithText("Good evening").assertIsDisplayed()
//        }
//    }
// }
