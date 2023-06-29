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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import dev.shorthouse.cryptodata.model.Coin
import dev.shorthouse.cryptodata.ui.component.LoadingIndicator
import dev.shorthouse.cryptodata.ui.preview.ListUiStatePreviewProvider
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
        uiState = uiState,
        onItemClick = { coin ->
            navController.navigate(Screen.DetailScreen.route + "/${coin.id}")
        }

    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    uiState: ListUiState,
    onItemClick: (Coin) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    when (uiState) {
        is ListUiState.Loading -> LoadingIndicator()
        is ListUiState.Error -> Text("error")
        is ListUiState.Success -> {
            val coinListItems = uiState.coins

            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                text = "Crypto Data",
                                style = MaterialTheme.typography.titleLarge
                            )
                        },
                        scrollBehavior = scrollBehavior
                    )
                },
                content = { scaffoldPadding ->
                    LazyColumn(
                        contentPadding = scaffoldPadding,
                        modifier = modifier.fillMaxSize()
                    ) {
                        items(
                            count = coinListItems.size,
                            key = { coinListItems[it].id },
                            itemContent = { index ->
                                val coinListItem = coinListItems[index]

                                CoinListItem(
                                    coin = coinListItem,
                                    onItemClick = { onItemClick(coinListItem) }
                                )
                            }
                        )
                    }
                },
                modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
            )
        }
    }
}

@Composable
@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
private fun ListScreenPreview(
    @PreviewParameter(ListUiStatePreviewProvider::class) uiState: ListUiState,
) {
    AppTheme {
        ListScreen(
            uiState = uiState,
            onItemClick = {}
        )
    }
}
