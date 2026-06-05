package dev.shorthouse.coinwatch.ui.screen.pulse

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.shorthouse.coinwatch.R
import dev.shorthouse.coinwatch.model.FearGreed
import dev.shorthouse.coinwatch.model.FearGreedMoodBand
import dev.shorthouse.coinwatch.model.GlobalMarket
import dev.shorthouse.coinwatch.model.TrendingCoin
import dev.shorthouse.coinwatch.ui.component.ErrorState
import dev.shorthouse.coinwatch.ui.component.LoadingIndicator
import dev.shorthouse.coinwatch.ui.insets.AppWindowInsets
import dev.shorthouse.coinwatch.ui.preview.AppPreviewWrapper
import dev.shorthouse.coinwatch.ui.preview.PulseUiStatePreviewProvider
import dev.shorthouse.coinwatch.ui.screen.pulse.component.GlobalMarketCard
import dev.shorthouse.coinwatch.ui.screen.pulse.component.MarketMoodCard
import dev.shorthouse.coinwatch.ui.screen.pulse.component.PulseSection
import dev.shorthouse.coinwatch.ui.screen.pulse.component.TrendingNow
import dev.shorthouse.coinwatch.ui.theme.FearAmber
import dev.shorthouse.coinwatch.ui.theme.GreedLightGreen
import dev.shorthouse.coinwatch.ui.theme.NegativeRed
import dev.shorthouse.coinwatch.ui.theme.PositiveGreen
import dev.shorthouse.coinwatch.ui.theme.ZeroWhite
import kotlinx.collections.immutable.ImmutableList

@Composable
fun PulseScreen(
    onNavigateDetails: (String) -> Unit,
    viewModel: PulseViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    PulseScreen(
        uiState = uiState,
        onCoinClick = { coinId -> onNavigateDetails(coinId) },
        onRefresh = { viewModel.refreshUiState() },
        onDismissError = { errorMessageId -> viewModel.dismissErrorMessage(errorMessageId) },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PulseScreen(
    uiState: PulseUiState,
    onCoinClick: (String) -> Unit,
    onRefresh: () -> Unit,
    onDismissError: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }
    val pullRefreshState = rememberPullToRefreshState()

    Scaffold(
        topBar = {
            PulseTopBar(
                moodBand = uiState.fearGreed?.moodBand,
                scrollBehavior = scrollBehavior
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        contentWindowInsets = AppWindowInsets.horizontalContent,
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { scaffoldPadding ->
        PullToRefreshBox(
            isRefreshing = uiState.isRefreshing,
            onRefresh = onRefresh,
            state = pullRefreshState,
            indicator = {
                PullToRefreshDefaults.Indicator(
                    modifier = Modifier.align(Alignment.TopCenter),
                    isRefreshing = uiState.isRefreshing,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    state = pullRefreshState
                )
            },
            modifier = Modifier
                .padding(scaffoldPadding)
                .fillMaxSize()
        ) {
            when {
                uiState.isLoading -> {
                    LoadingIndicator()
                }

                uiState.fearGreed == null && uiState.globalMarket == null -> {
                    ErrorState(
                        message = stringResource(R.string.error_pulse)
                    )
                }

                else -> {
                    PulseContent(
                        fearGreed = uiState.fearGreed,
                        globalMarket = uiState.globalMarket,
                        trendingCoins = uiState.trendingCoins,
                        onCoinClick = onCoinClick
                    )
                }
            }

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
private fun PulseTopBar(
    moodBand: FearGreedMoodBand?,
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.animateContentSize()
            ) {
                moodBand?.let { band ->
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(
                                when (band) {
                                    FearGreedMoodBand.ExtremeFear -> NegativeRed
                                    FearGreedMoodBand.Fear -> FearAmber
                                    FearGreedMoodBand.Neutral -> ZeroWhite
                                    FearGreedMoodBand.Greed -> GreedLightGreen
                                    FearGreedMoodBand.ExtremeGreed -> PositiveGreen
                                }
                            )
                    )
                }

                Text(
                    text = stringResource(R.string.pulse_screen),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
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
private fun PulseContent(
    fearGreed: FearGreed?,
    globalMarket: GlobalMarket?,
    trendingCoins: ImmutableList<TrendingCoin>,
    onCoinClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
    ) {
        fearGreed?.let {
            PulseSection(title = stringResource(R.string.pulse_market_mood)) {
                MarketMoodCard(fearGreed = it)
            }
        }

        globalMarket?.let {
            PulseSection(title = stringResource(R.string.pulse_global_market)) {
                GlobalMarketCard(globalMarket = it)
            }
        }

        if (trendingCoins.isNotEmpty()) {
            PulseSection(title = stringResource(R.string.pulse_trending_now)) {
                TrendingNow(
                    trendingCoins = trendingCoins,
                    onCoinClick = onCoinClick
                )
            }
        }
    }
}

@Composable
@Preview
@PreviewWrapper(wrapper = AppPreviewWrapper::class)
private fun PulseScreenPreview(
    @PreviewParameter(PulseUiStatePreviewProvider::class) uiState: PulseUiState,
) {
    PulseScreen(
        uiState = uiState,
        onCoinClick = {},
        onRefresh = {},
        onDismissError = {}
    )
}
