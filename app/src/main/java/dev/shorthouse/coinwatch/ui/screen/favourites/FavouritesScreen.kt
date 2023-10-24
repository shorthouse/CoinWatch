package dev.shorthouse.coinwatch.ui.screen.favourites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import dev.shorthouse.coinwatch.navigation.Screen
import dev.shorthouse.coinwatch.ui.component.ErrorState
import dev.shorthouse.coinwatch.ui.previewdata.FavouritesUiStatePreviewProvider
import dev.shorthouse.coinwatch.ui.screen.favourites.component.FavouritesEmptyState
import dev.shorthouse.coinwatch.ui.screen.favourites.component.FavouritesSkeletonLoader
import dev.shorthouse.coinwatch.ui.screen.list.component.CoinFavouriteItem
import dev.shorthouse.coinwatch.ui.theme.AppTheme
import kotlinx.collections.immutable.ImmutableList

@Composable
fun FavouritesScreen(
    navController: NavController,
    viewModel: FavouritesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    FavouriteScreen(
        uiState = uiState,
        onCoinClick = { coin ->
            navController.navigate(Screen.Details.route + "/${coin.id}")
        },
        onRefresh = { viewModel.initialiseUiState() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FavouriteScreen(
    uiState: FavouritesUiState,
    onCoinClick: (Coin) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    when (uiState) {
        is FavouritesUiState.Success -> {
            Scaffold(
                topBar = {
                    FavouritesTopBar(
                        scrollBehavior = scrollBehavior
                    )
                },
                content = { scaffoldPadding ->
                    FavouritesContent(
                        favouriteCoins = uiState.favouriteCoins,
                        onCoinClick = onCoinClick,
                        modifier = Modifier.padding(scaffoldPadding)
                    )
                },
                modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
            )
        }

        is FavouritesUiState.Loading -> {
            FavouritesSkeletonLoader()
        }

        is FavouritesUiState.Error -> {
            ErrorState(
                message = uiState.message,
                onRetry = onRefresh
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FavouritesTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.favourites_screen),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.offset(x = (-4).dp)
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            scrolledContainerColor = MaterialTheme.colorScheme.background
        ),
        scrollBehavior = scrollBehavior,
        modifier = modifier
    )
}

@Composable
private fun FavouritesContent(
    favouriteCoins: ImmutableList<Coin>,
    onCoinClick: (Coin) -> Unit,
    modifier: Modifier = Modifier
) {
    if (favouriteCoins.isEmpty()) {
        FavouritesEmptyState(modifier = Modifier.fillMaxSize())
    } else {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 140.dp),
            contentPadding = PaddingValues(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = modifier
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
    }
}

@Composable
@Preview(showBackground = true)
private fun FavouritesScreenPreview(
    @PreviewParameter(FavouritesUiStatePreviewProvider::class) uiState: FavouritesUiState
) {
    AppTheme {
        FavouriteScreen(
            uiState = uiState,
            onCoinClick = {},
            onRefresh = {}
        )
    }
}
