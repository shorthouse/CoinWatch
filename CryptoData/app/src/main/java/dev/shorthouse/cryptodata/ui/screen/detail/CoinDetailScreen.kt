package dev.shorthouse.cryptodata.ui.screen.detail

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
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
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
import dev.shorthouse.cryptodata.ui.component.LoadingIndicator
import dev.shorthouse.cryptodata.ui.component.PercentageChangeChip
import dev.shorthouse.cryptodata.ui.component.PriceGraph
import dev.shorthouse.cryptodata.ui.previewdata.CoinDetailUiStatePreviewProvider
import dev.shorthouse.cryptodata.ui.screen.detail.component.CoinDetailListItem
import dev.shorthouse.cryptodata.ui.theme.AppTheme
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days

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
    onClickChartPeriod: (Duration) -> Unit,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        is CoinDetailUiState.Loading -> LoadingIndicator()
        is CoinDetailUiState.Error -> Text("error")
        is CoinDetailUiState.Success -> {
            Scaffold(
                topBar = {
                    TopAppBar(
                        navigationIcon = {
                            IconButton(onClick = onNavigateUp) {
                                Icon(
                                    imageVector = Icons.Rounded.ArrowBack,
                                    contentDescription = stringResource(R.string.cd_top_bar_back),
                                    tint = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        },
                        title = {},
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
                modifier = modifier
            )
        }
    }
}

@Composable
private fun CoinDetailContent(
    coinDetail: CoinDetail,
    coinChart: CoinChart,
    chartPeriod: Duration,
    onClickChartPeriod: (Duration) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
    ) {
        Surface(shape = MaterialTheme.shapes.medium) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp)
            ) {
                Column {
                    Text(
                        text = coinDetail.name,
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Text(
                        text = coinDetail.symbol,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Spacer(Modifier.weight(1f))
                AsyncImage(
                    model = coinDetail.image,
                    contentDescription = null,
                    modifier = Modifier.size(54.dp)
                )
            }
        }

        Surface(shape = MaterialTheme.shapes.medium) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(vertical = 16.dp)
            ) {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
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
                            text = "Last ${chartPeriod.inWholeDays} days",
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

                val chartPeriodOptions = remember {
                    listOf(
                        1.days,
                        7.days,
                        30.days,
                        365.days
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    chartPeriodOptions.forEach { chartPeriodOption ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .selectable(
                                    selected = (chartPeriodOption == chartPeriod),
                                    onClick = { onClickChartPeriod(chartPeriodOption) },
                                    role = Role.RadioButton
                                )
                        ) {
                            RadioButton(
                                selected = (chartPeriodOption == chartPeriod),
                                onClick = null,
                                modifier = Modifier.padding(8.dp)
                            )

                            Text(
                                text = chartPeriodOption.inWholeDays.toString(),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
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
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.card_header_details),
                    style = MaterialTheme.typography.titleMedium
                )

                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    coinDetailItems.forEach { coinDetailListItem ->
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

//        CoinChartCard(
//            coinDetail = coinDetail,
//            coinChart = coinChart,
//            chartPeriod = chartPeriod,
//            onClickChartPeriod = onClickChartPeriod
//        )
//
//        CoinDetailCard(
//            coinDetail = coinDetail
//        )

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
