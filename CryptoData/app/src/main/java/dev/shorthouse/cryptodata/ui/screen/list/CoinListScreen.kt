package dev.shorthouse.cryptodata.ui.screen.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.TrendingDown
import androidx.compose.material.icons.rounded.TrendingUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import dev.shorthouse.cryptodata.R
import dev.shorthouse.cryptodata.model.Coin
import dev.shorthouse.cryptodata.model.MarketStats
import dev.shorthouse.cryptodata.model.Percentage
import dev.shorthouse.cryptodata.model.Price
import dev.shorthouse.cryptodata.ui.component.ErrorState
import dev.shorthouse.cryptodata.ui.model.TimeOfDay
import dev.shorthouse.cryptodata.ui.previewdata.CoinListUiStatePreviewProvider
import dev.shorthouse.cryptodata.ui.screen.Screen
import dev.shorthouse.cryptodata.ui.screen.list.component.CoinFavouriteItem
import dev.shorthouse.cryptodata.ui.screen.list.component.CoinGeckoAttribution
import dev.shorthouse.cryptodata.ui.screen.list.component.CoinListItem
import dev.shorthouse.cryptodata.ui.screen.list.component.CoinListSkeletonLoader
import dev.shorthouse.cryptodata.ui.screen.list.component.CoinListTitle
import dev.shorthouse.cryptodata.ui.theme.AppTheme
import java.math.BigDecimal

@Composable
fun CoinListScreen(
    navController: NavController,
    viewModel: CoinListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CoinListScreen(
        uiState = uiState,
        onCoinClick = { coin ->
            navController.navigate(Screen.DetailScreen.route + "/${coin.id}")
        },
        onErrorRetry = { viewModel.initialiseUiState() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinListScreen(
    uiState: CoinListUiState,
    onCoinClick: (Coin) -> Unit,
    onErrorRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    when (uiState) {
        is CoinListUiState.Success -> {
            Scaffold(
                topBar = {
                    CoinListTopBar(
                        marketStats = uiState.marketStats,
                        timeOfDay = uiState.timeOfDay,
                        scrollBehavior = scrollBehavior
                    )
                },
                content = { scaffoldPadding ->
                    CoinListContent(
                        coins = uiState.coins,
                        favouriteCoins = uiState.favouriteCoins,
                        onCoinClick = onCoinClick,
                        modifier = Modifier.padding(scaffoldPadding)
                    )
                },
                modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
            )
        }
        is CoinListUiState.Loading -> {
            CoinListSkeletonLoader()
        }
        is CoinListUiState.Error -> ErrorState(
            message = uiState.message,
            onRetry = onErrorRetry
        )
    }
}

@Composable
private fun CoinListContent(
    coins: List<Coin>,
    favouriteCoins: List<Coin>,
    onCoinClick: (Coin) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(12.dp),
        modifier = modifier
    ) {
        item {
            CoinListTitle(
                text = stringResource(R.string.header_favourites)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                items(
                    count = favouriteCoins.size,
                    key = { favouriteCoins[it].id },
                    itemContent = { index ->
                        val favouriteCoinItem = favouriteCoins[index]

                        CoinFavouriteItem(
                            coin = favouriteCoinItem,
                            onCoinClick = { onCoinClick(favouriteCoinItem) }
                        )
                    }
                )
            }

            CoinListTitle(
                text = stringResource(R.string.header_coins)
            )
        }

        items(
            count = coins.size,
            key = { coins[it].id },
            itemContent = { index ->
                val coinListItem = coins[index]

                val cardShape = when (index) {
                    0 -> MaterialTheme.shapes.medium.copy(
                        bottomStart = CornerSize(0.dp),
                        bottomEnd = CornerSize(0.dp)
                    )
                    coins.lastIndex -> MaterialTheme.shapes.medium.copy(
                        topStart = CornerSize(0.dp),
                        topEnd = CornerSize(0.dp)
                    )
                    else -> RoundedCornerShape(0.dp)
                }

                CoinListItem(
                    coin = coinListItem,
                    onCoinClick = { onCoinClick(coinListItem) },
                    cardShape = cardShape
                )
            }
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun CoinListTopBar(
    marketStats: MarketStats,
    timeOfDay: TimeOfDay,
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(end = 12.dp)
            ) {
                Column {
                    Text(
                        text = when (timeOfDay) {
                            TimeOfDay.Morning -> stringResource(R.string.good_morning)
                            TimeOfDay.Afternoon -> stringResource(R.string.good_afternoon)
                            TimeOfDay.Evening -> stringResource(R.string.good_evening)
                        },
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (marketStats.marketCapChangePercentage24h.isPositive) {
                                stringResource(R.string.market_up)
                            } else {
                                stringResource(R.string.market_down)
                            },
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(Modifier.width(4.dp))

                        Icon(
                            imageVector = if (marketStats.marketCapChangePercentage24h.isPositive) {
                                Icons.Rounded.TrendingUp
                            } else {
                                Icons.Rounded.TrendingDown
                            },
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(Modifier.weight(1f))

                CoinGeckoAttribution()
            }
        },
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            scrolledContainerColor = MaterialTheme.colorScheme.background
        ),
        scrollBehavior = scrollBehavior,
        modifier = modifier
    )
}

@Composable
@Preview(showBackground = true)
private fun ListScreenPreview(
    @PreviewParameter(CoinListUiStatePreviewProvider::class) uiState: CoinListUiState
) {
    AppTheme {
        CoinListScreen(
            uiState = CoinListUiState.Success(
                coins = listOf(
                    Coin(
                        id = "bitcoin",
                        symbol = "BTC",
                        name = "Bitcoin",
                        image = "https://assets.coingecko.com/coins/images/1/large/bitcoin.png?1547033579",
                        currentPrice = Price(BigDecimal("30752")),
                        priceChangePercentage24h = Percentage(BigDecimal("-1.39")),
                        marketCapRank = 1,
                        prices24h = emptyList()
                    ),
                    Coin(
                        id = "ethereum",
                        symbol = "ETH",
                        name = "Ethereum",
                        image = "https://assets.coingecko.com/coins/images/279/large/ethereum.png?1595348880",
                        currentPrice = Price(BigDecimal("1345.62")),
                        priceChangePercentage24h = Percentage(BigDecimal("0.42")),
                        marketCapRank = 2,
                        prices24h = emptyList()
                    ),
                    Coin(
                        id = "tether",
                        symbol = "USDT",
                        name = "Tether",
                        image = "https://assets.coingecko.com/coins/images/325/large/Tether.png?1668148663",
                        currentPrice = Price(BigDecimal("1.0")),
                        priceChangePercentage24h = Percentage(BigDecimal("0.00")),
                        marketCapRank = 3,
                        prices24h = emptyList()
                    )
                ),
                favouriteCoins = listOf(
                    Coin(
                        id = "bitcoin",
                        symbol = "BTC",
                        name = "Bitcoin",
                        image = "https://assets.coingecko.com/coins/images/1/large/bitcoin.png?1547033579",
                        currentPrice = Price(BigDecimal("30752")),
                        priceChangePercentage24h = Percentage(BigDecimal("-1.39")),
                        marketCapRank = 1,
                        prices24h = emptyList()
                    ),
                    Coin(
                        id = "ethereum",
                        symbol = "ETH",
                        name = "Ethereum",
                        image = "https://assets.coingecko.com/coins/images/279/large/ethereum.png?1595348880",
                        currentPrice = Price(BigDecimal("1345.62")),
                        priceChangePercentage24h = Percentage(BigDecimal("0.42")),
                        marketCapRank = 2,
                        prices24h = emptyList()
                    ),
                    Coin(
                        id = "tether",
                        symbol = "USDT",
                        name = "Tether",
                        image = "https://assets.coingecko.com/coins/images/325/large/Tether.png?1668148663",
                        currentPrice = Price(BigDecimal("1.0")),
                        priceChangePercentage24h = Percentage(BigDecimal("0.00")),
                        marketCapRank = 3,
                        prices24h = emptyList()
                    )
                ),
                marketStats = MarketStats(
                    marketCapChangePercentage24h = Percentage(BigDecimal("-0.23"))
                ),
                timeOfDay = TimeOfDay.Evening
            ),
            onCoinClick = {},
            onErrorRetry = {}
        )
    }
}
