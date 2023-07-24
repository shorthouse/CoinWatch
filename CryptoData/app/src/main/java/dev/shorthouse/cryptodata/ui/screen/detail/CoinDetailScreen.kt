package dev.shorthouse.cryptodata.ui.screen.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarOutline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import coil.compose.AsyncImage
import dev.shorthouse.cryptodata.R
import dev.shorthouse.cryptodata.model.CoinChart
import dev.shorthouse.cryptodata.model.CoinDetail
import dev.shorthouse.cryptodata.ui.component.ErrorState
import dev.shorthouse.cryptodata.ui.model.ChartPeriod
import dev.shorthouse.cryptodata.ui.previewdata.CoinDetailUiStatePreviewProvider
import dev.shorthouse.cryptodata.ui.screen.detail.component.ChartCard
import dev.shorthouse.cryptodata.ui.screen.detail.component.ChartRangeCard
import dev.shorthouse.cryptodata.ui.screen.detail.component.CoinDetailSkeletonLoader
import dev.shorthouse.cryptodata.ui.screen.detail.component.MarketStatsCard
import dev.shorthouse.cryptodata.ui.theme.AppTheme

@Composable
fun CoinDetailScreen(
    navController: NavController,
    viewModel: CoinDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CoinDetailScreen(
        uiState = uiState,
        onNavigateUp = { navController.navigateUp() },
        onClickFavouriteCoin = { viewModel.toggleIsCoinFavourite() },
        onClickChartPeriod = { viewModel.updateChartPeriod(it) },
        onErrorRetry = { viewModel.initialiseUiState() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinDetailScreen(
    uiState: CoinDetailUiState,
    onNavigateUp: () -> Unit,
    onClickFavouriteCoin: () -> Unit,
    onClickChartPeriod: (ChartPeriod) -> Unit,
    onErrorRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    when (uiState) {
        is CoinDetailUiState.Success -> {
            Scaffold(
                topBar = {
                    ChartDetailTopBar(
                        coinName = uiState.coinDetail.name,
                        coinSymbol = uiState.coinDetail.symbol,
                        coinImage = uiState.coinDetail.image,
                        isCoinFavourite = uiState.isCoinFavourite,
                        onNavigateUp = onNavigateUp,
                        onClickFavouriteCoin = onClickFavouriteCoin,
                        scrollBehavior = scrollBehavior
                    )
                },
                content = { scaffoldPadding ->
                    CoinDetailContent(
                        coinDetail = uiState.coinDetail,
                        coinChart = uiState.coinChart,
                        chartPeriod = uiState.chartPeriod,
                        onClickChartPeriod = onClickChartPeriod,
                        modifier = Modifier.padding(scaffoldPadding)
                    )
                },
                modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
            )
        }
        is CoinDetailUiState.Loading -> {
            CoinDetailSkeletonLoader()
        }
        is CoinDetailUiState.Error -> {
            ErrorState(
                message = uiState.message,
                onRetry = onErrorRetry
            )
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun ChartDetailTopBar(
    coinName: String,
    coinSymbol: String,
    coinImage: String,
    isCoinFavourite: Boolean,
    onNavigateUp: () -> Unit,
    onClickFavouriteCoin: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier
) {
    LargeTopAppBar(
        navigationIcon = {
            IconButton(onClick = onNavigateUp) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = stringResource(R.string.cd_top_bar_back)
                )
            }
        },
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text(
                        text = coinName
                    )
                    Text(
                        text = coinSymbol,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(Modifier.weight(1f))

                AsyncImage(
                    model = coinImage,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .size(54.dp)
                )
            }
        },
        actions = {
            IconButton(onClick = onClickFavouriteCoin) {
                Icon(
                    imageVector = if (isCoinFavourite) {
                        Icons.Rounded.Star
                    } else {
                        Icons.Rounded.StarOutline
                    },
                    contentDescription = stringResource(R.string.cd_top_bar_favourite),
                    tint = MaterialTheme.colorScheme.onBackground
                )
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
private fun CoinDetailContent(
    coinDetail: CoinDetail,
    coinChart: CoinChart,
    chartPeriod: ChartPeriod,
    onClickChartPeriod: (ChartPeriod) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
    ) {
        ChartCard(
            coinDetail = coinDetail,
            coinChart = coinChart,
            chartPeriod = chartPeriod,
            onClickChartPeriod = onClickChartPeriod
        )

        ChartRangeCard(
            coinDetail = coinDetail,
            coinChart = coinChart
        )

        MarketStatsCard(
            coinDetail = coinDetail
        )
    }
}

@Composable
@Preview
private fun DetailScreenPreview(
    @PreviewParameter(CoinDetailUiStatePreviewProvider::class) uiState: CoinDetailUiState
) {
    AppTheme {
        CoinDetailScreen(
            uiState = uiState,
            onNavigateUp = {},
            onClickFavouriteCoin = {},
            onClickChartPeriod = {},
            onErrorRetry = {}
        )
    }
}
