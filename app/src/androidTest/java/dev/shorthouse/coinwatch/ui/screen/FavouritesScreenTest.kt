//package dev.shorthouse.coinwatch.ui.screen
//
//import androidx.compose.ui.semantics.SemanticsProperties
//import androidx.compose.ui.test.SemanticsMatcher
//import androidx.compose.ui.test.assertIsDisplayed
//import androidx.compose.ui.test.junit4.createComposeRule
//import androidx.compose.ui.test.onNodeWithContentDescription
//import androidx.compose.ui.test.onNodeWithTag
//import androidx.compose.ui.test.onNodeWithText
//import androidx.compose.ui.test.onParent
//import androidx.compose.ui.test.performClick
//import androidx.compose.ui.test.performScrollToIndex
//import androidx.compose.ui.test.performTouchInput
//import androidx.compose.ui.test.swipeDown
//import com.google.common.truth.Truth.assertThat
//import dev.shorthouse.coinwatch.R
//import dev.shorthouse.coinwatch.data.source.local.model.FavouriteCoin
//import dev.shorthouse.coinwatch.model.Percentage
//import dev.shorthouse.coinwatch.model.Price
//import dev.shorthouse.coinwatch.ui.screen.favourites.FavouriteScreen
//import dev.shorthouse.coinwatch.ui.screen.favourites.FavouritesUiState
//import dev.shorthouse.coinwatch.ui.theme.AppTheme
//import kotlinx.collections.immutable.persistentListOf
//import kotlinx.collections.immutable.toPersistentList
//import org.junit.Rule
//import org.junit.Test
//import java.math.BigDecimal
//
//class FavouritesScreenTest {
//
//    @get:Rule
//    val composeTestRule = createComposeRule()
//
//    @Test
//    fun when_uiStateLoading_should_showLoadingIndicator() {
//        val uiState = FavouritesUiState(
//            isLoading = true
//        )
//
//        composeTestRule.setContent {
//            AppTheme {
//                FavouriteScreen(
//                    uiState = uiState,
//                    onCoinClick = {},
//                    onUpdateIsFavouritesCondensed = {},
//                    onRefresh = {},
//                    onDismissError = {},
//                )
//            }
//        }
//
//        composeTestRule.apply {
//            onNode(SemanticsMatcher.keyIsDefined(SemanticsProperties.ProgressBarRangeInfo))
//                .assertIsDisplayed()
//        }
//    }
//
//    @Test
//    fun when_uiStateSuccess_should_showExpectedContent() {
//        val uiStateSuccess = FavouritesUiState()
//
//        composeTestRule.setContent {
//            AppTheme {
//                FavouriteScreen(
//                    uiState = uiStateSuccess,
//                    onCoinClick = {},
//                    onUpdateIsFavouritesCondensed = {},
//                    onRefresh = {},
//                    onDismissError = {},
//                )
//            }
//        }
//
//        composeTestRule.apply {
//            onNodeWithText("Favourites").assertIsDisplayed()
//        }
//    }
//
//    @Test
//    fun when_noFavouriteCoins_should_displayEmptyState() {
//        val uiStateSuccess = FavouritesUiState(
//            favouriteCoins = persistentListOf()
//        )
//
//        composeTestRule.setContent {
//            AppTheme {
//                FavouriteScreen(
//                    uiState = uiStateSuccess,
//                    onCoinClick = {},
//                    onUpdateIsFavouritesCondensed = {},
//                    onRefresh = {},
//                    onDismissError = {},
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
//    fun when_favouriteCoinsExist_should_displayFavouriteCoins() {
//        val uiStateSuccess = FavouritesUiState(
//            favouriteCoins = persistentListOf(
//                FavouriteCoin(
//                    id = "bitcoin",
//                    symbol = "BTC",
//                    name = "Bitcoin",
//                    imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
//                    currentPrice = Price("29446.336548759988"),
//                    priceChangePercentage24h = Percentage("1.76833"),
//                    prices24h = persistentListOf(
//                        BigDecimal("29390.15178296929"),
//                        BigDecimal("29428.222505493162"),
//                        BigDecimal("29475.12359313808"),
//                        BigDecimal("29471.20179209623")
//                    )
//                ),
//                FavouriteCoin(
//                    id = "ethereum",
//                    symbol = "ETH",
//                    name = "Ethereum",
//                    imageUrl = "https://cdn.coinranking.com/rk4RKHOuW/eth.svg",
//                    currentPrice = Price("1875.473083380222"),
//                    priceChangePercentage24h = Percentage("-1.84"),
//                    prices24h = persistentListOf(
//                        BigDecimal("1872.5227299255032"),
//                        BigDecimal("1874.813847463032"),
//                        BigDecimal("1877.1265051203513"),
//                        BigDecimal("1879.89804628163")
//                    )
//                )
//            )
//        )
//
//        composeTestRule.setContent {
//            AppTheme {
//                FavouriteScreen(
//                    uiState = uiStateSuccess,
//                    onCoinClick = {},
//                    onUpdateIsFavouritesCondensed = {},
//                    onRefresh = {},
//                    onDismissError = {},
//                )
//            }
//        }
//
//        composeTestRule.apply {
//            onNodeWithText("Bitcoin").assertIsDisplayed()
//            onNodeWithText("BTC").assertIsDisplayed()
//            onNodeWithText("$29,446.34").assertIsDisplayed()
//            onNodeWithText("+1.77%").assertIsDisplayed()
//            onNodeWithTag("priceGraph BTC", useUnmergedTree = true).assertIsDisplayed()
//
//            onNodeWithText("Ethereum").assertIsDisplayed()
//            onNodeWithText("ETH").assertIsDisplayed()
//            onNodeWithText("$1,875.47").assertIsDisplayed()
//            onNodeWithText("-1.84%").assertIsDisplayed()
//            onNodeWithTag("priceGraph ETH", useUnmergedTree = true).assertIsDisplayed()
//        }
//    }
//
//    @Test
//    fun when_favouriteCoinsExistAndListCondensed_should_displayCondensedFavouriteCoins() {
//        val uiStateSuccess = FavouritesUiState(
//            isFavouritesCondensed = true,
//            favouriteCoins = persistentListOf(
//                FavouriteCoin(
//                    id = "bitcoin",
//                    symbol = "BTC",
//                    name = "Bitcoin",
//                    imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
//                    currentPrice = Price("29446.336548759988"),
//                    priceChangePercentage24h = Percentage("1.76833"),
//                    prices24h = persistentListOf(
//                        BigDecimal("29390.15178296929"),
//                        BigDecimal("29428.222505493162"),
//                        BigDecimal("29475.12359313808"),
//                        BigDecimal("29471.20179209623")
//                    )
//                ),
//                FavouriteCoin(
//                    id = "ethereum",
//                    symbol = "ETH",
//                    name = "Ethereum",
//                    imageUrl = "https://cdn.coinranking.com/rk4RKHOuW/eth.svg",
//                    currentPrice = Price("1875.473083380222"),
//                    priceChangePercentage24h = Percentage("-1.84"),
//                    prices24h = persistentListOf(
//                        BigDecimal("1872.5227299255032"),
//                        BigDecimal("1874.813847463032"),
//                        BigDecimal("1877.1265051203513"),
//                        BigDecimal("1879.89804628163")
//                    )
//                )
//            )
//        )
//
//        composeTestRule.setContent {
//            AppTheme {
//                FavouriteScreen(
//                    uiState = uiStateSuccess,
//                    onCoinClick = {},
//                    onUpdateIsFavouritesCondensed = {},
//                    onRefresh = {},
//                    onDismissError = {},
//                )
//            }
//        }
//
//        composeTestRule.apply {
//            onNodeWithText("Bitcoin").assertIsDisplayed()
//            onNodeWithText("BTC").assertIsDisplayed()
//            onNodeWithText("$29,446.34").assertIsDisplayed()
//            onNodeWithText("+1.77%").assertIsDisplayed()
//            onNodeWithTag("priceGraph BTC", useUnmergedTree = true).assertDoesNotExist()
//
//            onNodeWithText("Ethereum").assertIsDisplayed()
//            onNodeWithText("ETH").assertIsDisplayed()
//            onNodeWithText("$1,875.47").assertIsDisplayed()
//            onNodeWithText("-1.84%").assertIsDisplayed()
//            onNodeWithTag("priceGraph ETH", useUnmergedTree = true).assertDoesNotExist()
//        }
//    }
//
//    @Test
//    fun when_clickingCoinItem_should_callOnCoinClick() {
//        var onCoinClickCalled = false
//
//        val uiStateSuccess = FavouritesUiState(
//            favouriteCoins = persistentListOf(
//                FavouriteCoin(
//                    id = "bitcoin",
//                    symbol = "BTC",
//                    name = "Bitcoin",
//                    imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
//                    currentPrice = Price("29446.336548759988"),
//                    priceChangePercentage24h = Percentage("1.76833"),
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
//                FavouriteScreen(
//                    uiState = uiStateSuccess,
//                    onCoinClick = { onCoinClickCalled = true },
//                    onUpdateIsFavouritesCondensed = {},
//                    onRefresh = {},
//                    onDismissError = {},
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
//    fun when_clickingToggleFavouritesCondensed_should_callOnUpdateIsFavouritesCondensed() {
//        var onUpdateIsFavouritesCondensedCalled = false
//
//        val uiStateSuccess = FavouritesUiState(
//            isFavouriteCoinsEmpty = false
//        )
//
//        composeTestRule.setContent {
//            AppTheme {
//                FavouriteScreen(
//                    uiState = uiStateSuccess,
//                    onCoinClick = {},
//                    onUpdateIsFavouritesCondensed = { onUpdateIsFavouritesCondensedCalled = true },
//                    onRefresh = {},
//                    onDismissError = {},
//                )
//            }
//        }
//
//        composeTestRule.apply {
//            onNodeWithContentDescription("Condense favourites list").performClick()
//        }
//
//        assertThat(onUpdateIsFavouritesCondensedCalled).isTrue()
//    }
//
//    @Test
//    fun when_scrollingFavouritesList_should_showScrollToTopFab() {
//        val favouriteCoins = (1..20).map {
//            FavouriteCoin(
//                id = it.toString(),
//                symbol = "",
//                name = it.toString(),
//                imageUrl = "",
//                currentPrice = Price(null),
//                priceChangePercentage24h = Percentage(null),
//                prices24h = persistentListOf()
//            )
//        }.toPersistentList()
//
//        val uiState = FavouritesUiState(
//            favouriteCoins = favouriteCoins
//        )
//
//        composeTestRule.setContent {
//            AppTheme {
//                FavouriteScreen(
//                    uiState = uiState,
//                    onCoinClick = {},
//                    onUpdateIsFavouritesCondensed = {},
//                    onRefresh = {},
//                    onDismissError = {},
//                )
//            }
//        }
//
//        composeTestRule.apply {
//            onNodeWithContentDescription("Scroll to top").assertDoesNotExist()
//            onNodeWithText("1").onParent().performScrollToIndex(favouriteCoins.size - 1)
//            onNodeWithContentDescription("Scroll to top").assertIsDisplayed()
//        }
//    }
//
//    @Test
//    fun when_scrollingFavouritesCondensedList_should_showScrollToTopFab() {
//        val favouriteCoins = (1..20).map {
//            FavouriteCoin(
//                id = it.toString(),
//                symbol = "",
//                name = it.toString(),
//                imageUrl = "",
//                currentPrice = Price(null),
//                priceChangePercentage24h = Percentage(null),
//                prices24h = persistentListOf()
//            )
//        }.toPersistentList()
//
//        val uiState = FavouritesUiState(
//            favouriteCoins = favouriteCoins,
//            isFavouritesCondensed = true
//        )
//
//        composeTestRule.setContent {
//            AppTheme {
//                FavouriteScreen(
//                    uiState = uiState,
//                    onCoinClick = {},
//                    onUpdateIsFavouritesCondensed = {},
//                    onRefresh = {},
//                    onDismissError = {},
//                )
//            }
//        }
//
//        composeTestRule.apply {
//            onNodeWithContentDescription("Scroll to top").assertDoesNotExist()
//            onNodeWithText("1").onParent().performScrollToIndex(favouriteCoins.size - 1)
//            onNodeWithContentDescription("Scroll to top").assertIsDisplayed()
//        }
//    }
//
//    @Test
//    fun when_pullRefreshingScreen_should_callONRefresh() {
//        var onRefreshCalled = false
//
//        val uiState = FavouritesUiState(
//            favouriteCoins = persistentListOf(
//                FavouriteCoin(
//                    id = "bitcoin",
//                    symbol = "BTC",
//                    name = "Bitcoin",
//                    imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
//                    currentPrice = Price("29446.336548759988"),
//                    priceChangePercentage24h = Percentage("1.76833"),
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
//                FavouriteScreen(
//                    uiState = uiState,
//                    onCoinClick = {},
//                    onUpdateIsFavouritesCondensed = {},
//                    onRefresh = { onRefreshCalled = true },
//                    onDismissError = {},
//                )
//            }
//        }
//
//        composeTestRule.apply {
//            onNodeWithText("Bitcoin")
//                .onParent()
//                .performTouchInput {
//                    swipeDown()
//                }
//        }
//
//        assertThat(onRefreshCalled).isTrue()
//    }
//
//    @Test
//    fun when_errorMessageListNotEmpty_should_showErrorMessageSnackbar() {
//        val uiState = FavouritesUiState(
//            errorMessageIds = listOf(
//                R.string.error_network_favourite_coins,
//                R.string.error_local_favourite_coins
//            )
//        )
//
//        composeTestRule.setContent {
//            AppTheme {
//                FavouriteScreen(
//                    uiState = uiState,
//                    onCoinClick = {},
//                    onUpdateIsFavouritesCondensed = {},
//                    onRefresh = {},
//                    onDismissError = {},
//                )
//            }
//        }
//
//        composeTestRule.apply {
//            onNodeWithText("Latest favourite coin data unavailable").assertIsDisplayed()
//        }
//    }
//
//    @Test
//    fun when_favouriteCoinsListEmpty_should_notShowCondenseListButton() {
//        val uiState = FavouritesUiState(
//            isFavouriteCoinsEmpty = true
//        )
//
//        composeTestRule.setContent {
//            AppTheme {
//                FavouriteScreen(
//                    uiState = uiState,
//                    onCoinClick = {},
//                    onUpdateIsFavouritesCondensed = {},
//                    onRefresh = {},
//                    onDismissError = {},
//                )
//            }
//        }
//
//        composeTestRule.apply {
//            onNodeWithContentDescription("Condense favourites list").assertDoesNotExist()
//            onNodeWithContentDescription("Expand favourites list").assertDoesNotExist()
//        }
//    }
//
//    @Test
//    fun when_favouriteCoinsListNotEmpty_should_showCondenseListButton() {
//        val uiState = FavouritesUiState(
//            isFavouriteCoinsEmpty = false
//        )
//
//        composeTestRule.setContent {
//            AppTheme {
//                FavouriteScreen(
//                    uiState = uiState,
//                    onCoinClick = {},
//                    onUpdateIsFavouritesCondensed = {},
//                    onRefresh = {},
//                    onDismissError = {},
//                )
//            }
//        }
//
//        composeTestRule.apply {
//            onNodeWithContentDescription("Condense favourites list").assertIsDisplayed()
//        }
//    }
//}
