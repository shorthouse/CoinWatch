package dev.shorthouse.cryptodata.ui.screen.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import dev.shorthouse.cryptodata.R
import dev.shorthouse.cryptodata.model.Coin
import dev.shorthouse.cryptodata.model.Percentage
import dev.shorthouse.cryptodata.model.Price
import dev.shorthouse.cryptodata.ui.component.LoadingIndicator
import dev.shorthouse.cryptodata.ui.previewdata.CoinListUiStatePreviewProvider
import dev.shorthouse.cryptodata.ui.screen.Screen
import dev.shorthouse.cryptodata.ui.screen.list.component.CoinFavouriteItem
import dev.shorthouse.cryptodata.ui.screen.list.component.CoinListItem
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
        onItemClick = { coin ->
            navController.navigate(Screen.DetailScreen.route + "/${coin.id}")
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinListScreen(
    uiState: CoinListUiState,
    onItemClick: (Coin) -> Unit,
    modifier: Modifier = Modifier
) {
    // val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    when (uiState) {
        is CoinListUiState.Success -> {
            Scaffold(
                topBar = {
//                    CoinListTopBar(
//                        scrollBehavior = scrollBehavior
//                    )
                },
                content = { scaffoldPadding ->
                    CoinListContent(
                        coins = uiState.coins,
                        onItemClick = onItemClick,
                        modifier = Modifier.padding(scaffoldPadding)
                    )
                },
                // modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
            )
        }
        is CoinListUiState.Loading -> LoadingIndicator()
        is CoinListUiState.Error -> Text("error")
    }
}

@Composable
private fun CoinListContent(
    coins: List<Coin>,
    modifier: Modifier,
    onItemClick: (Coin) -> Unit
) {
    Column(modifier = modifier.fillMaxSize().padding(12.dp)) {
        Text(
            text = stringResource(R.string.header_favourites),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.height(8.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                count = coins.size,
                key = { coins[it].id },
                itemContent = { index ->
                    CoinFavouriteItem(coin = coins[index])
                }
            )
        }

        Spacer(Modifier.height(32.dp))

        Text(
            text = stringResource(R.string.header_coins),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.height(8.dp))
        LazyColumn {
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

                    if (index != 0) {
                        Divider(
                            color = MaterialTheme.colorScheme.background,
                            modifier = Modifier.padding(horizontal = 40.dp)
                        )
                    }

                    CoinListItem(
                        coin = coinListItem,
                        onItemClick = { onItemClick(coinListItem) },
                        cardShape = cardShape
                    )
                }
            )
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun CoinListTopBar(scrollBehavior: TopAppBarScrollBehavior) {
    MediumTopAppBar(
        title = {
            Text(
                text = stringResource(R.string.top_app_bar_title_market),
                color = MaterialTheme.colorScheme.onBackground
            )
        },
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            scrolledContainerColor = MaterialTheme.colorScheme.background
        ),
        scrollBehavior = scrollBehavior,
        modifier = Modifier.background(Color.Green)
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
                )
            ),
            onItemClick = {}
        )
    }
}
