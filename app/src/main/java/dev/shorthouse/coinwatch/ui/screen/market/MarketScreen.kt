package dev.shorthouse.coinwatch.ui.screen.market

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardDoubleArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.shorthouse.coinwatch.R
import dev.shorthouse.coinwatch.model.Coin
import dev.shorthouse.coinwatch.ui.component.ErrorState
import dev.shorthouse.coinwatch.ui.previewdata.MarketUiStatePreviewProvider
import dev.shorthouse.coinwatch.ui.screen.market.component.MarketCoinItem
import dev.shorthouse.coinwatch.ui.screen.market.component.MarketEmptyState
import dev.shorthouse.coinwatch.ui.screen.market.component.MarketSkeletonLoader
import dev.shorthouse.coinwatch.ui.screen.market.component.SearchPrompt
import dev.shorthouse.coinwatch.ui.theme.AppTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.launch

@Composable
fun MarketScreen(
    viewModel: MarketViewModel = hiltViewModel(),
    onNavigateDetails: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MarketScreen(
        uiState = uiState,
        onCoinClick = { coin ->
            onNavigateDetails(coin.id)
        },
        onRefresh = { viewModel.initialiseUiState() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketScreen(
    uiState: MarketUiState,
    onCoinClick: (Coin) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val lazyListState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val showJumpToTopFab by remember {
        derivedStateOf {
            lazyListState.firstVisibleItemIndex > 0
        }
    }

    Scaffold(
        topBar = {
            MarketTopBar(scrollBehavior = scrollBehavior)
        },
        content = { scaffoldPadding ->
            when (uiState) {
                is MarketUiState.Success -> {
                    MarketContent(
                        coins = uiState.coins,
                        onCoinClick = onCoinClick,
                        lazyListState = lazyListState,
                        modifier = Modifier.padding(scaffoldPadding)
                    )
                }

                is MarketUiState.Error -> {
                    ErrorState(
                        message = uiState.message,
                        onRetry = onRefresh,
                        modifier = Modifier.padding(scaffoldPadding)
                    )
                }

                is MarketUiState.Loading -> {
                    MarketSkeletonLoader(modifier = Modifier.padding(scaffoldPadding))
                }
            }
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = showJumpToTopFab,
                enter = scaleIn(),
                exit = scaleOut()
            ) {
                SmallFloatingActionButton(
                    onClick = {
                        scope.launch {
                            scrollBehavior.state.heightOffset = 0f
                            lazyListState.animateScrollToItem(0)
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 12.dp
                    ),
                    content = {
                        Icon(
                            imageVector = Icons.Rounded.KeyboardDoubleArrowUp,
                            contentDescription = stringResource(R.string.cd_list_scroll_top)
                        )
                    }
                )
            }
        },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MarketTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.market_screen),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
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
fun MarketContent(
    coins: ImmutableList<Coin>,
    onCoinClick: (Coin) -> Unit,
    lazyListState: LazyListState,
    modifier: Modifier = Modifier
) {
    if (coins.isEmpty()) {
        MarketEmptyState(
            modifier = modifier
                .fillMaxSize()
                .padding(12.dp)
        )
    } else {
        LazyColumn(
            state = lazyListState,
            contentPadding = PaddingValues(horizontal = 12.dp),
            modifier = modifier.fillMaxSize()
        ) {
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

                    MarketCoinItem(
                        coin = coinListItem,
                        onCoinClick = { onCoinClick(coinListItem) },
                        cardShape = cardShape
                    )
                }
            )

            item {
                SearchPrompt(modifier = Modifier.padding(vertical = 12.dp))
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun MarketScreenPreview(
    @PreviewParameter(MarketUiStatePreviewProvider::class) uiState: MarketUiState
) {
    AppTheme {
        MarketScreen(
            uiState = uiState,
            onCoinClick = {},
            onRefresh = {}
        )
    }
}
