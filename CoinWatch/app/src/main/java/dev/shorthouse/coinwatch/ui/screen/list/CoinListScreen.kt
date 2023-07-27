package dev.shorthouse.coinwatch.ui.screen.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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
import dev.shorthouse.coinwatch.R
import dev.shorthouse.coinwatch.model.Coin
import dev.shorthouse.coinwatch.model.MarketStats
import dev.shorthouse.coinwatch.ui.component.ErrorState
import dev.shorthouse.coinwatch.ui.model.TimeOfDay
import dev.shorthouse.coinwatch.ui.previewdata.CoinListUiStatePreviewProvider
import dev.shorthouse.coinwatch.ui.screen.Screen
import dev.shorthouse.coinwatch.ui.screen.list.component.CoinFavouriteItem
import dev.shorthouse.coinwatch.ui.screen.list.component.CoinGeckoAttribution
import dev.shorthouse.coinwatch.ui.screen.list.component.CoinListItem
import dev.shorthouse.coinwatch.ui.screen.list.component.CoinListSkeletonLoader
import dev.shorthouse.coinwatch.ui.screen.list.component.FavouriteCoinEmptyState
import dev.shorthouse.coinwatch.ui.theme.AppTheme

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
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
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
            Text(
                text = stringResource(R.string.header_favourites),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            if (favouriteCoins.isEmpty()) {
                FavouriteCoinEmptyState()
            } else {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
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
            }

            Spacer(Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.header_coins),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
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
@Preview(showBackground = true)
private fun ListScreenPreview(
    @PreviewParameter(CoinListUiStatePreviewProvider::class) uiState: CoinListUiState
) {
    AppTheme {
        CoinListScreen(
            uiState = uiState,
            onCoinClick = {},
            onErrorRetry = {}
        )
    }
}
