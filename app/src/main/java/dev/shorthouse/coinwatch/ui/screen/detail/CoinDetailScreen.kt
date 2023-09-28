package dev.shorthouse.coinwatch.ui.screen.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import dev.shorthouse.coinwatch.R
import dev.shorthouse.coinwatch.model.CoinChart
import dev.shorthouse.coinwatch.model.CoinDetail
import dev.shorthouse.coinwatch.ui.component.ErrorState
import dev.shorthouse.coinwatch.ui.component.PriceGraph
import dev.shorthouse.coinwatch.ui.model.ChartPeriod
import dev.shorthouse.coinwatch.ui.previewdata.CoinDetailUiStatePreviewProvider
import dev.shorthouse.coinwatch.ui.screen.detail.component.ChartPeriodSelector
import dev.shorthouse.coinwatch.ui.screen.detail.component.ChartRangeLine
import dev.shorthouse.coinwatch.ui.screen.detail.component.CoinDetailSkeletonLoader
import dev.shorthouse.coinwatch.ui.screen.detail.component.EmptyChartMessage
import dev.shorthouse.coinwatch.ui.screen.detail.component.EmptyChartRangeMessage
import dev.shorthouse.coinwatch.ui.screen.detail.component.MarketStatsCard
import dev.shorthouse.coinwatch.ui.screen.detail.component.PercentageChangeChip
import dev.shorthouse.coinwatch.ui.theme.AppTheme

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
                        coinImage = uiState.coinDetail.imageUrl,
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
    val context = LocalContext.current

    val imageBuilder = remember(context) {
        ImageRequest.Builder(context = context)
            .decoderFactory(factory = SvgDecoder.Factory())
    }

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
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = coinName,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = coinSymbol,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                AsyncImage(
                    model = imageBuilder
                        .data(coinImage)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .size(44.dp)
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
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
            .testTag("coin_detail_content")
    ) {
        Surface(shape = MaterialTheme.shapes.medium) {
            Column(
                verticalArrangement = Arrangement.spacedBy(32.dp),
                modifier = Modifier.padding(vertical = 12.dp)
            ) {
                Column(modifier = Modifier.padding(horizontal = 12.dp)) {
                    Text(
                        text = coinDetail.currentPrice.formattedAmount,
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Spacer(Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        PercentageChangeChip(
                            percentage = coinChart.periodPriceChangePercentage
                        )

                        Spacer(Modifier.width(8.dp))

                        Text(
                            text = stringResource(chartPeriod.longNameId),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                if (coinChart.prices.isNotEmpty()) {
                    PriceGraph(
                        prices = coinChart.prices,
                        priceChangePercentage = coinChart.periodPriceChangePercentage,
                        isGraphAnimated = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )

                    ChartPeriodSelector(
                        selectedChartPeriod = chartPeriod,
                        onChartPeriodSelected = onClickChartPeriod,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                } else {
                    EmptyChartMessage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(278.dp)
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.title_chart_range),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Surface(shape = MaterialTheme.shapes.medium) {
            if (coinChart.prices.isNotEmpty()) {
                Column(modifier = Modifier.padding(12.dp)) {
                    ChartRangeLine(
                        currentPrice = coinDetail.currentPrice,
                        minPrice = coinChart.minPrice,
                        maxPrice = coinChart.maxPrice,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(20.dp)
                            .padding(horizontal = 4.dp)
                    )

                    Spacer(Modifier.height(4.dp))

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(horizontalAlignment = Alignment.Start) {
                            Text(
                                text = stringResource(R.string.range_title_low),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Text(
                                text = coinChart.minPrice.formattedAmount,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = stringResource(R.string.range_title_high),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Text(
                                text = coinChart.maxPrice.formattedAmount,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            } else {
                EmptyChartRangeMessage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(91.dp)
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.card_header_market_stats),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
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
