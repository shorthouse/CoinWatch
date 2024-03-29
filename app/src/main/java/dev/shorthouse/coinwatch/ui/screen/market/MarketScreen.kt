package dev.shorthouse.coinwatch.ui.screen.market

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.TrendingDown
import androidx.compose.material.icons.automirrored.rounded.TrendingFlat
import androidx.compose.material.icons.automirrored.rounded.TrendingUp
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.shorthouse.coinwatch.R
import dev.shorthouse.coinwatch.data.preferences.global.CoinSort
import dev.shorthouse.coinwatch.data.source.local.model.Coin
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.ui.component.LoadingIndicator
import dev.shorthouse.coinwatch.ui.component.ScrollToTopFab
import dev.shorthouse.coinwatch.ui.component.pullrefresh.PullRefreshIndicator
import dev.shorthouse.coinwatch.ui.component.pullrefresh.pullRefresh
import dev.shorthouse.coinwatch.ui.component.pullrefresh.rememberPullRefreshState
import dev.shorthouse.coinwatch.ui.model.TimeOfDay
import dev.shorthouse.coinwatch.ui.previewdata.MarketUiStatePreviewProvider
import dev.shorthouse.coinwatch.ui.screen.market.component.CoinSortBottomSheet
import dev.shorthouse.coinwatch.ui.screen.market.component.CoinsEmptyState
import dev.shorthouse.coinwatch.ui.screen.market.component.MarketChip
import dev.shorthouse.coinwatch.ui.screen.market.component.MarketCoinItem
import dev.shorthouse.coinwatch.ui.screen.market.component.SearchPrompt
import dev.shorthouse.coinwatch.ui.theme.AppTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.launch

@Composable
fun MarketScreen(
    onNavigateDetails: (String) -> Unit,
    onNavigateSettings: () -> Unit,
    viewModel: MarketViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MarketScreen(
        uiState = uiState,
        onCoinClick = { coin ->
            onNavigateDetails(coin.id)
        },
        onNavigateSettings = onNavigateSettings,
        onUpdateCoinSort = { coinSort ->
            viewModel.updateCoinSort(coinSort)
        },
        onUpdateIsCoinSortSheetShown = { showSheet ->
            viewModel.updateIsCoinSortSheetShown(showSheet)
        },
        onRefresh = {
            viewModel.pullRefreshCoins()
        },
        onDismissError = { errorMessageId ->
            viewModel.dismissErrorMessage(errorMessageId)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketScreen(
    uiState: MarketUiState,
    onCoinClick: (Coin) -> Unit,
    onNavigateSettings: () -> Unit,
    onUpdateCoinSort: (CoinSort) -> Unit,
    onUpdateIsCoinSortSheetShown: (Boolean) -> Unit,
    onRefresh: () -> Unit,
    onDismissError: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val listState = rememberLazyListState()
    val coinSortSheetState = rememberModalBottomSheetState()
    val snackbarHostState = remember { SnackbarHostState() }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState.isRefreshing,
        onRefresh = onRefresh
    )
    val showScrollToTopFab by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 1
        }
    }

    Scaffold(
        topBar = {
            MarketTopBar(
                timeOfDay = uiState.timeOfDay,
                marketCapChangePercentage24h = uiState.marketCapChangePercentage24h,
                onNavigateSettings = onNavigateSettings,
                scrollBehavior = scrollBehavior
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            AnimatedVisibility(
                visible = showScrollToTopFab,
                enter = scaleIn(),
                exit = scaleOut()
            ) {
                ScrollToTopFab(
                    onClick = {
                        scope.launch {
                            listState.animateScrollToItem(0)
                        }
                    }
                )
            }
        },
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { scaffoldPadding ->
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .pullRefresh(pullRefreshState)
                .padding(scaffoldPadding)
        ) {
            when {
                uiState.isLoading -> {
                    LoadingIndicator()
                }

                else -> {
                    MarketContent(
                        coins = uiState.coins,
                        onCoinClick = onCoinClick,
                        coinSort = uiState.coinSort,
                        onUpdateIsCoinSortSheetShown = onUpdateIsCoinSortSheetShown,
                        lazyListState = listState
                    )

                    if (uiState.isCoinSortSheetShown) {
                        CoinSortBottomSheet(
                            sheetState = coinSortSheetState,
                            selectedCoinSort = uiState.coinSort,
                            onCoinSortSelected = { coinSort ->
                                onUpdateCoinSort(coinSort)

                                scope.launch {
                                    coinSortSheetState.hide()
                                }.invokeOnCompletion {
                                    if (!coinSortSheetState.isVisible) {
                                        onUpdateIsCoinSortSheetShown(false)
                                    }
                                }
                            },
                            onDismissRequest = { onUpdateIsCoinSortSheetShown(false) }
                        )
                    }
                }
            }

            PullRefreshIndicator(
                refreshing = uiState.isRefreshing,
                state = pullRefreshState,
                backgroundColor = MaterialTheme.colorScheme.primaryContainer
            )

            if (uiState.errorMessageIds.isNotEmpty()) {
                val errorMessage = stringResource(uiState.errorMessageIds.first())

                LaunchedEffect(errorMessage, snackbarHostState) {
                    snackbarHostState.showSnackbar(message = errorMessage)
                    onDismissError(uiState.errorMessageIds.first())
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MarketTopBar(
    timeOfDay: TimeOfDay,
    marketCapChangePercentage24h: Percentage?,
    onNavigateSettings: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier
) {
    var menuExpanded by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Column(modifier = Modifier.animateContentSize()) {
                Text(
                    text = stringResource(R.string.time_of_day_prefix_good) +
                        " " + timeOfDay.name.lowercase(),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                marketCapChangePercentage24h?.let {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = when {
                                it.isPositive -> stringResource(R.string.market_is_up)
                                it.isNegative -> stringResource(R.string.market_is_down)
                                else -> stringResource(R.string.market_is_flat)
                            },
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(Modifier.width(4.dp))

                        Icon(
                            imageVector = when {
                                it.isPositive -> Icons.AutoMirrored.Rounded.TrendingUp
                                it.isNegative -> Icons.AutoMirrored.Rounded.TrendingDown
                                else -> Icons.AutoMirrored.Rounded.TrendingFlat
                            },
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            contentDescription = null
                        )
                    }
                }
            }
        },
        actions = {
            IconButton(onClick = { menuExpanded = !menuExpanded }) {
                Icon(
                    imageVector = Icons.Rounded.MoreVert,
                    contentDescription = stringResource(R.string.cd_more),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false }
            ) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(R.string.dropdown_option_settings),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    onClick = onNavigateSettings
                )
            }
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
    coinSort: CoinSort,
    onUpdateIsCoinSortSheetShown: (Boolean) -> Unit,
    lazyListState: LazyListState,
    modifier: Modifier = Modifier
) {
    if (coins.isEmpty()) {
        CoinsEmptyState()
    } else {
        LazyColumn(
            state = lazyListState,
            contentPadding = PaddingValues(start = 12.dp, end = 12.dp),
            modifier = modifier
        ) {
            item {
                MarketChip(
                    label = stringResource(coinSort.nameId),
                    onClick = { onUpdateIsCoinSortSheetShown(true) }
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
            onNavigateSettings = {},
            onUpdateCoinSort = {},
            onUpdateIsCoinSortSheetShown = {},
            onRefresh = {},
            onDismissError = {}
        )
    }
}
