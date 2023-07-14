package dev.shorthouse.cryptodata.ui.screen.detail

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import dev.shorthouse.cryptodata.model.ChartPeriod
import dev.shorthouse.cryptodata.model.CoinChart
import dev.shorthouse.cryptodata.model.CoinDetail
import dev.shorthouse.cryptodata.ui.component.LoadingIndicator
import dev.shorthouse.cryptodata.ui.component.PercentageChangeChip
import dev.shorthouse.cryptodata.ui.component.PriceGraph
import dev.shorthouse.cryptodata.ui.previewdata.CoinDetailUiStatePreviewProvider
import dev.shorthouse.cryptodata.ui.screen.detail.component.ChartPeriodSelector
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
        onClickChartPeriod = { viewModel.updateChartPeriod(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinDetailScreen(
    uiState: CoinDetailUiState,
    onNavigateUp: () -> Unit,
    onClickChartPeriod: (ChartPeriod) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val isTopBarCollapsed by remember {
        derivedStateOf { scrollBehavior.state.collapsedFraction > 0.8f }
    }

    when (uiState) {
        is CoinDetailUiState.Loading -> LoadingIndicator()
        is CoinDetailUiState.Error -> Text("error")
        is CoinDetailUiState.Success -> {
            Scaffold(
                topBar = {
                    LargeTopAppBar(
                        navigationIcon = {
                            IconButton(onClick = onNavigateUp) {
                                Icon(
                                    imageVector = Icons.Rounded.ArrowBack,
                                    contentDescription = stringResource(R.string.cd_top_bar_back),
                                    tint = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        },
                        title = {
                            Surface(
                                shape = MaterialTheme.shapes.medium,
                                color = if (isTopBarCollapsed) {
                                    MaterialTheme.colorScheme.background
                                } else {
                                    MaterialTheme.colorScheme.surface
                                },
                                modifier = Modifier.padding(end = 16.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(12.dp)
                                ) {
                                    Column {
                                        Text(
                                            text = uiState.coinDetail.name,
                                            style = MaterialTheme.typography.titleMedium,
                                        )
                                        Text(
                                            text = uiState.coinDetail.symbol,
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        )
                                    }
                                    Spacer(Modifier.weight(1f))
                                    AsyncImage(
                                        model = uiState.coinDetail.image,
                                        contentDescription = null,
                                        modifier = Modifier.size(54.dp)
                                    )
                                }
                            }
                        },
                        scrollBehavior = scrollBehavior,
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.background
                        )
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
    }
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
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(start = 16.dp, top = 4.dp, end = 16.dp, bottom = 16.dp)
    ) {
        Surface(shape = MaterialTheme.shapes.medium) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(vertical = 12.dp)
            ) {
                Column(modifier = Modifier.padding(horizontal = 12.dp)) {
                    Text(
                        text = coinDetail.currentPrice.formattedAmount,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        PercentageChangeChip(
                            percentage = coinChart.periodPriceChangePercentage
                        )
                        Text(
                            text = stringResource(chartPeriod.longNameId),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }

                PriceGraph(
                    prices = coinChart.prices,
                    priceChangePercentage = coinChart.periodPriceChangePercentage,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )

                ChartPeriodSelector(
                    selectedChartPeriod = chartPeriod,
                    onChartPeriodSelected = onClickChartPeriod,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            }
        }

//        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
//            Surface(
//                shape = MaterialTheme.shapes.medium,
//                modifier = Modifier.weight(1f)
//            ) {
//                Column(
//                    verticalArrangement = Arrangement.spacedBy(4.dp),
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    modifier = Modifier.padding(16.dp)
//                ) {
//                    Text(
//                        text = stringResource(R.string.subtitle_period_low),
//                        color = MaterialTheme.colorScheme.onSurfaceVariant,
//                        style = MaterialTheme.typography.bodyMedium
//                    )
//                    Text(
//                        text = coinChart.minPrice.formattedAmount,
//                        style = MaterialTheme.typography.bodyMedium
//                    )
//                    PercentageChange(
//                        percentage = coinChart.minPriceChangePercentage
//                    )
//                }
//            }
//            Surface(
//                shape = MaterialTheme.shapes.medium,
//                modifier = Modifier.weight(1f)
//            ) {
//                Column(
//                    verticalArrangement = Arrangement.spacedBy(4.dp),
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    modifier = Modifier.padding(16.dp)
//                ) {
//                    Text(
//                        text = stringResource(R.string.subtitle_period_high),
//                        color = MaterialTheme.colorScheme.onSurfaceVariant,
//                        style = MaterialTheme.typography.bodyMedium
//                    )
//                    Text(
//                        text = coinChart.maxPrice.formattedAmount,
//                        style = MaterialTheme.typography.bodyMedium
//                    )
//                    PercentageChange(
//                        percentage = coinChart.maxPriceChangePercentage
//                    )
//                }
//            }
//        }

        val coinDetailItems = remember(coinDetail) {
            listOf(
                CoinDetailListItem(
                    nameId = R.string.list_item_market_cap_rank,
                    value = coinDetail.marketCapRank
                ),
                CoinDetailListItem(
                    nameId = R.string.list_item_market_cap,
                    value = coinDetail.marketCap.formattedAmount
                ),
                CoinDetailListItem(
                    nameId = R.string.list_item_circulating_supply,
                    value = coinDetail.circulatingSupply
                ),
                CoinDetailListItem(
                    nameId = R.string.list_item_ath,
                    value = coinDetail.allTimeHigh.formattedAmount
                ),
                CoinDetailListItem(
                    nameId = R.string.list_item_ath_date,
                    value = coinDetail.allTimeHighDate
                ),
                CoinDetailListItem(
                    nameId = R.string.list_item_atl,
                    value = coinDetail.allTimeLow.formattedAmount
                ),
                CoinDetailListItem(
                    nameId = R.string.list_item_atl_date,
                    value = coinDetail.allTimeLowDate
                )
            )
        }

        Surface(shape = MaterialTheme.shapes.medium) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = stringResource(R.string.card_header_details),
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(Modifier.height(16.dp))

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    coinDetailItems.forEachIndexed { coinDetailIndex, coinDetailListItem ->
                        if (coinDetailIndex != 0) {
                            Divider()
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = stringResource(coinDetailListItem.nameId),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = coinDetailListItem.value,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }
    }
}

data class CoinDetailListItem(
    @StringRes val nameId: Int,
    val value: String
)

@Composable
@Preview(showBackground = true)
private fun DetailScreenPreview(
    @PreviewParameter(CoinDetailUiStatePreviewProvider::class) uiState: CoinDetailUiState
) {
    AppTheme {
        CoinDetailScreen(
            uiState = uiState,
            onNavigateUp = {},
            onClickChartPeriod = {}
        )
    }
}
