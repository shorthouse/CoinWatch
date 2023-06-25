package dev.shorthouse.cryptodata.ui.screen.list

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import dev.shorthouse.cryptodata.model.Coin
import dev.shorthouse.cryptodata.ui.component.LoadingIndicator
import dev.shorthouse.cryptodata.ui.screen.Screen
import dev.shorthouse.cryptodata.ui.screen.list.component.CoinListItem
import dev.shorthouse.cryptodata.ui.theme.AppTheme

@Composable
fun ListScreen(
    navController: NavController,
    viewModel: ListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ListScreen(
        cryptocurrencies = uiState.coins,
        isLoading = uiState.isLoading,
        error = uiState.error,
        onItemClick = {
            navController.navigate(Screen.DetailScreen.route + "/${it.id}")
        }

    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    cryptocurrencies: List<Coin>,
    isLoading: Boolean,
    error: String?,
    onItemClick: (Coin) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Crypto Data",
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                scrollBehavior = scrollBehavior
            )
        },
        content = { scaffoldPadding ->
            if (isLoading) {
                LoadingIndicator()
            } else if (!error.isNullOrBlank()) {
                Text(text = error)
            } else {
                LazyColumn(
                    contentPadding = scaffoldPadding,
                    modifier = modifier.fillMaxSize()
                ) {
                    items(
                        count = cryptocurrencies.size,
                        key = { cryptocurrencies[it].id },
                        itemContent = { index ->
                            val cryptocurrency = cryptocurrencies[index]

                            CoinListItem(
                                coin = cryptocurrency,
                                onItemClick = { onItemClick(cryptocurrency) }
                            )
                        }
                    )
                }
            }
        },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    )
}

@Composable
@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
private fun ListScreenPreview() {
    AppTheme {
        ListScreen(
            cryptocurrencies = listOf(
                Coin(
                    id = "ethereum",
                    symbol = "ETH",
                    name = "Ethereum",
                    image = "https://assets.coingecko.com/coins/images/279/large/ethereum.png?1595348880",
                    currentPrice = 1345.62,
                    priceChangePercentage = 0.42
                )
            ),
            isLoading = false,
            error = null,
            onItemClick = {}
        )
    }
}
