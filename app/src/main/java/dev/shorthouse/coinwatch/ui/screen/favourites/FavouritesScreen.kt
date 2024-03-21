package dev.shorthouse.coinwatch.ui.screen.favourites

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material.icons.rounded.ViewStream
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import dev.shorthouse.coinwatch.data.source.local.model.FavouriteCoin
import dev.shorthouse.coinwatch.ui.component.LoadingIndicator
import dev.shorthouse.coinwatch.ui.component.ScrollToTopFab
import dev.shorthouse.coinwatch.ui.component.pullrefresh.PullRefreshIndicator
import dev.shorthouse.coinwatch.ui.component.pullrefresh.pullRefresh
import dev.shorthouse.coinwatch.ui.component.pullrefresh.rememberPullRefreshState
import dev.shorthouse.coinwatch.ui.previewdata.FavouritesUiStatePreviewProvider
import dev.shorthouse.coinwatch.ui.screen.favourites.component.FavouriteCondensedItem
import dev.shorthouse.coinwatch.ui.screen.favourites.component.FavouriteItem
import dev.shorthouse.coinwatch.ui.screen.favourites.component.FavouritesEmptyState
import dev.shorthouse.coinwatch.ui.theme.AppTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.launch

@Composable
fun FavouritesScreen(
    viewModel: FavouritesViewModel = hiltViewModel(),
    onNavigateDetails: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    FavouriteScreen(
        uiState = uiState,
        onCoinClick = { coin ->
            onNavigateDetails(coin.id)
        },
        onUpdateIsFavouritesCondensed = { isCondensed ->
            viewModel.updateIsFavouritesCondensed(isCondensed = isCondensed)
        },
        onRefresh = {
            viewModel.pullRefreshFavouriteCoins()
        },
        onDismissError = { errorMessageId ->
            viewModel.dismissErrorMessage(errorMessageId)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouriteScreen(
    uiState: FavouritesUiState,
    onCoinClick: (FavouriteCoin) -> Unit,
    onUpdateIsFavouritesCondensed: (Boolean) -> Unit,
    onRefresh: () -> Unit,
    onDismissError: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val listState = rememberLazyListState()
    val gridState = rememberLazyGridState()
    val snackbarHostState = remember { SnackbarHostState() }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState.isRefreshing,
        onRefresh = onRefresh
    )
    val showScrollToTopFab by remember {
        derivedStateOf {
            gridState.firstVisibleItemIndex > 0 || listState.firstVisibleItemIndex > 0
        }
    }

    LaunchedEffect(uiState.isFavouritesCondensed) {
        if (uiState.isFavouritesCondensed) {
            gridState.scrollToItem(0)
        } else {
            listState.scrollToItem(0)
        }
    }

    Scaffold(
        topBar = {
            FavouritesTopBar(
                isFavouritesCondensed = uiState.isFavouritesCondensed,
                onUpdateIsFavouritesCondensed = onUpdateIsFavouritesCondensed,
                isFavouritesListEmpty = uiState.isFavouriteCoinsEmpty,
                isLoading = uiState.isLoading,
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
                            if (uiState.isFavouritesCondensed) {
                                listState.animateScrollToItem(0)
                            } else {
                                gridState.animateScrollToItem(0)
                            }
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
                .padding(scaffoldPadding)
                .pullRefresh(pullRefreshState)
        ) {
            when {
                uiState.isLoading -> {
                    LoadingIndicator()
                }

                else -> {
                    FavouritesContent(
                        favouriteCoins = uiState.favouriteCoins,
                        onCoinClick = onCoinClick,
                        isFavouritesCondensed = uiState.isFavouritesCondensed,
                        gridState = gridState,
                        listState = listState
                    )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouritesTopBar(
    isFavouritesCondensed: Boolean,
    onUpdateIsFavouritesCondensed: (Boolean) -> Unit,
    isFavouritesListEmpty: Boolean,
    isLoading: Boolean,
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.favourites_screen),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        },
        actions = {
            if (!isLoading && !isFavouritesListEmpty) {
                IconButton(onClick = { onUpdateIsFavouritesCondensed(!isFavouritesCondensed) }) {
                    Icon(
                        imageVector = if (isFavouritesCondensed) {
                            Icons.Rounded.GridView
                        } else {
                            Icons.Rounded.ViewStream
                        },
                        contentDescription = if (isFavouritesCondensed) {
                            stringResource(R.string.cd_expand_favourites)
                        } else {
                            stringResource(R.string.cd_condense_favourites)
                        },
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FavouritesContent(
    favouriteCoins: ImmutableList<FavouriteCoin>,
    onCoinClick: (FavouriteCoin) -> Unit,
    isFavouritesCondensed: Boolean,
    gridState: LazyGridState,
    listState: LazyListState,
    modifier: Modifier = Modifier
) {
    CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
        when {
            favouriteCoins.isEmpty() -> {
                FavouritesEmptyState()
            }

            isFavouritesCondensed -> {
                FavouritesCondensedList(
                    favouriteCoins = favouriteCoins,
                    onCoinClick = onCoinClick,
                    listState = listState,
                    modifier = modifier
                )
            }

            else -> {
                FavouritesList(
                    favouriteCoins = favouriteCoins,
                    onCoinClick = onCoinClick,
                    gridState = gridState,
                    modifier = modifier
                )
            }
        }
    }
}

@Composable
fun FavouritesList(
    favouriteCoins: ImmutableList<FavouriteCoin>,
    onCoinClick: (FavouriteCoin) -> Unit,
    gridState: LazyGridState,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        state = gridState,
        columns = GridCells.Adaptive(minSize = 140.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(start = 12.dp, end = 12.dp, bottom = 12.dp),
        modifier = modifier.fillMaxHeight()
    ) {
        items(
            count = favouriteCoins.size,
            key = { favouriteCoins[it].id },
            itemContent = { index ->
                val favouriteCoinItem = favouriteCoins[index]

                FavouriteItem(
                    favouriteCoin = favouriteCoinItem,
                    onCoinClick = { onCoinClick(favouriteCoinItem) }
                )
            }
        )
    }
}

@Composable
fun FavouritesCondensedList(
    favouriteCoins: ImmutableList<FavouriteCoin>,
    onCoinClick: (FavouriteCoin) -> Unit,
    listState: LazyListState,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(start = 12.dp, end = 12.dp, bottom = 12.dp),
        modifier = modifier.fillMaxHeight()
    ) {
        items(
            count = favouriteCoins.size,
            key = { favouriteCoins[it].id },
            itemContent = { index ->
                val favouriteCoinItem = favouriteCoins[index]

                val cardShape = when {
                    favouriteCoins.size == 1 -> MaterialTheme.shapes.medium

                    index == 0 -> MaterialTheme.shapes.medium.copy(
                        bottomStart = CornerSize(0.dp),
                        bottomEnd = CornerSize(0.dp)
                    )

                    index == favouriteCoins.lastIndex -> MaterialTheme.shapes.medium.copy(
                        topStart = CornerSize(0.dp),
                        topEnd = CornerSize(0.dp)
                    )

                    else -> RoundedCornerShape(0.dp)
                }

                FavouriteCondensedItem(
                    favouriteCoin = favouriteCoinItem,
                    onCoinClick = { onCoinClick(favouriteCoinItem) },
                    cardShape = cardShape
                )
            }
        )
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
            onUpdateIsFavouritesCondensed = {},
            onRefresh = {},
            onDismissError = {},
        )
    }
}
