package dev.shorthouse.coinwatch.ui.screen.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardDoubleArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.navigation.NavController
import dev.shorthouse.coinwatch.R
import dev.shorthouse.coinwatch.model.Coin
import dev.shorthouse.coinwatch.navigation.Screen
import dev.shorthouse.coinwatch.ui.component.ErrorState
import dev.shorthouse.coinwatch.ui.model.TimeOfDay
import dev.shorthouse.coinwatch.ui.previewdata.CoinListUiStatePreviewProvider
import dev.shorthouse.coinwatch.ui.screen.list.component.CoinListItem
import dev.shorthouse.coinwatch.ui.screen.list.component.CoinListSkeletonLoader
import dev.shorthouse.coinwatch.ui.screen.list.component.CoinSearchPrompt
import dev.shorthouse.coinwatch.ui.screen.list.component.CoinsEmptyState
import dev.shorthouse.coinwatch.ui.theme.AppTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.launch

@Composable
fun CoinListScreen(
    navController: NavController,
    viewModel: CoinListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CoinListScreen(
        uiState = uiState,
        onCoinClick = { coin ->
            navController.navigate(Screen.Details.route + "/${coin.id}")
        },
        onRefresh = { viewModel.initialiseUiState() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CoinListScreen(
    uiState: CoinListUiState,
    onCoinClick: (Coin) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    when (uiState) {
        is CoinListUiState.Success -> {
            val lazyListState = rememberLazyListState()
            val scope = rememberCoroutineScope()
            val showJumpToTopFab by remember {
                derivedStateOf {
                    lazyListState.firstVisibleItemIndex > 0
                }
            }

            Scaffold(
                topBar = {
                    CoinListTopBar(
                        timeOfDay = uiState.timeOfDay,
                        scrollBehavior = scrollBehavior
                    )
                },
                content = { scaffoldPadding ->
                    CoinListContent(
                        coins = uiState.coins,
                        onCoinClick = onCoinClick,
                        lazyListState = lazyListState,
                        modifier = Modifier.padding(scaffoldPadding)
                    )
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
                            containerColor = MaterialTheme.colorScheme.background,
                            contentColor = MaterialTheme.colorScheme.onBackground,
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

        is CoinListUiState.Loading -> {
            CoinListSkeletonLoader()
        }

        is CoinListUiState.Error -> {
            ErrorState(
                message = uiState.message,
                onRetry = onRefresh
            )
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun CoinListTopBar(
    timeOfDay: TimeOfDay,
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(
                text = when (timeOfDay) {
                    TimeOfDay.Morning -> stringResource(R.string.good_morning)
                    TimeOfDay.Afternoon -> stringResource(R.string.good_afternoon)
                    TimeOfDay.Evening -> stringResource(R.string.good_evening)
                },
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
private fun CoinListContent(
    coins: ImmutableList<Coin>,
    onCoinClick: (Coin) -> Unit,
    lazyListState: LazyListState,
    modifier: Modifier = Modifier
) {
    if (coins.isEmpty()) {
        CoinsEmptyState()
    } else {
        LazyColumn(
            state = lazyListState,
            contentPadding = PaddingValues(horizontal = 12.dp),
            modifier = modifier
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

                    CoinListItem(
                        coin = coinListItem,
                        onCoinClick = { onCoinClick(coinListItem) },
                        cardShape = cardShape
                    )
                }
            )

            item {
                CoinSearchPrompt(modifier = Modifier.padding(vertical = 12.dp))
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun CoinListScreenPreview(
    @PreviewParameter(CoinListUiStatePreviewProvider::class) uiState: CoinListUiState
) {
    AppTheme {
        CoinListScreen(
            uiState = uiState,
            onCoinClick = {},
            onRefresh = {}
        )
    }
}
