package dev.shorthouse.coinwatch.ui.screen.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import dev.shorthouse.coinwatch.R
import dev.shorthouse.coinwatch.model.CoinChart
import dev.shorthouse.coinwatch.model.CoinDetails
import dev.shorthouse.coinwatch.ui.component.ErrorState
import dev.shorthouse.coinwatch.ui.component.LoadingIndicator
import dev.shorthouse.coinwatch.ui.model.ChartPeriod
import dev.shorthouse.coinwatch.ui.previewdata.DetailsUiStatePreviewProvider
import dev.shorthouse.coinwatch.ui.screen.details.component.CoinChartCard
import dev.shorthouse.coinwatch.ui.screen.details.component.CoinChartRangeCard
import dev.shorthouse.coinwatch.ui.screen.details.component.EmptyTopBar
import dev.shorthouse.coinwatch.ui.screen.details.component.MarketStatsCard
import dev.shorthouse.coinwatch.ui.theme.AppTheme

@Composable
fun DetailsScreen(
    viewModel: DetailsViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DetailsScreen(
        uiState = uiState,
        onNavigateUp = onNavigateUp,
        onClickFavouriteCoin = { viewModel.toggleIsCoinFavourite() },
        onClickChartPeriod = { chartPeriod ->
            viewModel.updateChartPeriod(chartPeriod)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    uiState: DetailsUiState,
    onNavigateUp: () -> Unit,
    onClickFavouriteCoin: () -> Unit,
    onClickChartPeriod: (ChartPeriod) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        topBar = {
            when (uiState) {
                is DetailsUiState.Success -> {
                    DetailsTopBar(
                        coinDetails = uiState.coinDetails,
                        isCoinFavourite = uiState.isCoinFavourite,
                        onNavigateUp = onNavigateUp,
                        onClickFavouriteCoin = onClickFavouriteCoin,
                        scrollBehavior = scrollBehavior
                    )
                }

                else -> {
                    EmptyTopBar(
                        onNavigateUp = onNavigateUp
                    )
                }
            }
        },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { scaffoldPadding ->
        when (uiState) {
            is DetailsUiState.Success -> {
                DetailsContent(
                    coinDetails = uiState.coinDetails,
                    coinChart = uiState.coinChart,
                    chartPeriod = uiState.chartPeriod,
                    onClickChartPeriod = onClickChartPeriod,
                    modifier = Modifier.padding(scaffoldPadding)
                )
            }

            is DetailsUiState.Error -> {
                ErrorState(
                    message = uiState.message,
                    modifier = Modifier.padding(scaffoldPadding)
                )
            }

            is DetailsUiState.Loading -> {
                LoadingIndicator()
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DetailsTopBar(
    coinDetails: CoinDetails,
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
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = stringResource(R.string.cd_top_bar_back)
                )
            }
        },
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = coinDetails.name,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = coinDetails.symbol,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                AsyncImage(
                    model = imageBuilder
                        .data(coinDetails.imageUrl)
                        .crossfade(true)
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
                        Icons.Rounded.Favorite
                    } else {
                        Icons.Rounded.FavoriteBorder
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
fun DetailsContent(
    coinDetails: CoinDetails,
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
    ) {
        CoinChartCard(
            currentPrice = coinDetails.currentPrice,
            prices = coinChart.prices,
            periodPriceChangePercentage = coinChart.periodPriceChangePercentage,
            chartPeriod = chartPeriod,
            onClickChartPeriod = onClickChartPeriod
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.title_chart_range),
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(Modifier.height(8.dp))

        CoinChartRangeCard(
            currentPrice = coinDetails.currentPrice,
            minPrice = coinChart.minPrice,
            maxPrice = coinChart.maxPrice,
            isPricesEmpty = coinChart.prices.isEmpty()
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.card_header_market_stats),
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(Modifier.height(8.dp))

        MarketStatsCard(coinDetails = coinDetails)
    }
}

@Composable
@Preview
private fun DetailsScreenPreview(
    @PreviewParameter(DetailsUiStatePreviewProvider::class) uiState: DetailsUiState
) {
    AppTheme {
        DetailsScreen(
            uiState = uiState,
            onNavigateUp = {},
            onClickFavouriteCoin = {},
            onClickChartPeriod = {}
        )
    }
}
