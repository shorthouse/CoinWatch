package dev.shorthouse.cryptodata.ui.screen.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.chart.scroll.rememberChartScrollSpec
import com.patrykandpatrick.vico.core.chart.values.AxisValuesOverrider
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.patrykandpatrick.vico.core.entry.entryOf
import dev.shorthouse.cryptodata.R
import dev.shorthouse.cryptodata.model.CoinChart
import dev.shorthouse.cryptodata.model.CoinDetail
import dev.shorthouse.cryptodata.ui.component.LoadingIndicator
import dev.shorthouse.cryptodata.ui.component.PercentageChange
import dev.shorthouse.cryptodata.ui.previewdata.CoinDetailUiStatePreviewProvider
import dev.shorthouse.cryptodata.ui.screen.detail.component.CoinDetailList
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
                    CoinDetailTopBar(
                        coinName = uiState.coinDetail.name,
                        coinSymbol = uiState.coinDetail.symbol,
                        onNavigateUp = onNavigateUp
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
@OptIn(ExperimentalMaterial3Api::class)
private fun CoinDetailTopBar(
    coinName: String,
    coinSymbol: String,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        navigationIcon = {
            IconButton(onClick = onNavigateUp) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = stringResource(R.string.cd_top_bar_back)
                )
            }
        },
        title = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = coinName,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = coinSymbol,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        },
        modifier = modifier
    )
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
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        AsyncImage(
            model = coinDetail.image,
            placeholder = painterResource(R.drawable.ic_launcher_background),
            contentDescription = stringResource(R.string.cd_coin_image),
            modifier = Modifier
                .padding(top = 8.dp)
                .size(64.dp)
        )

        Text(
            text = coinDetail.currentPrice.formattedAmount,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(Modifier.height(4.dp))

        PercentageChange(
            percentage = coinChart.periodPriceChangePercentage
        )

        Spacer(Modifier.height(32.dp))

        CoinPastPricesChart(
            coinPastPrices = coinChart.prices,
            minPrice = coinChart.minPrice.amount,
            maxPrice = coinChart.maxPrice.amount
        )

        Spacer(Modifier.height(24.dp))

        val chartPeriodOptions = remember {
            listOf(
                1.days,
                7.days,
                30.days,
                365.days
            )
        }

        Row {
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
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
            }
        }

        Row {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Period Low",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = coinChart.minPrice.formattedAmount,
                    style = MaterialTheme.typography.bodyLarge
                )
                PercentageChange(
                    percentage = coinChart.minPriceChangePercentage
                )
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Period High",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = coinChart.maxPrice.formattedAmount,
                    style = MaterialTheme.typography.bodyLarge
                )
                PercentageChange(
                    percentage = coinChart.maxPriceChangePercentage
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        CoinDetailList(
            title = stringResource(R.string.list_header_market_data),
            items = listOf(
                CoinDetailListItem(
                    name = stringResource(R.string.list_item_market_cap_rank),
                    value = coinDetail.marketCapRank
                ),
                CoinDetailListItem(
                    name = stringResource(R.string.list_item_market_cap),
                    value = coinDetail.marketCap.formattedAmount
                ),
                CoinDetailListItem(
                    name = stringResource(R.string.list_item_circulating_supply),
                    value = coinDetail.circulatingSupply
                )
            )
        )

        Spacer(Modifier.height(16.dp))

        CoinDetailList(
            title = stringResource(R.string.list_header_historic_data),
            items = listOf(
                CoinDetailListItem(
                    name = stringResource(R.string.list_item_atl),
                    value = coinDetail.allTimeLow.formattedAmount
                ),
                CoinDetailListItem(
                    name = stringResource(R.string.list_item_ath),
                    value = coinDetail.allTimeHigh.formattedAmount
                ),
                CoinDetailListItem(
                    name = stringResource(R.string.list_item_atl_date),
                    value = coinDetail.allTimeLowDate
                ),
                CoinDetailListItem(
                    name = stringResource(R.string.list_item_ath_date),
                    value = coinDetail.allTimeHighDate
                )
            )
        )

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun CoinPastPricesChart(
    coinPastPrices: List<Double>,
    minPrice: Double,
    maxPrice: Double,
    modifier: Modifier = Modifier
) {
    val chartModel = remember {
        entryModelOf(
            coinPastPrices.mapIndexed { index, historicalPrice ->
                entryOf(x = index, y = historicalPrice)
            }
        )
    }

    Chart(
        chart = lineChart(
            axisValuesOverrider = AxisValuesOverrider.fixed(
                minY = minPrice.toFloat(),
                maxY = maxPrice.toFloat()
            )
        ),
        model = chartModel,
        chartScrollSpec = rememberChartScrollSpec(
            isScrollEnabled = false
        ),
        modifier = modifier
    )
}

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
