package dev.shorthouse.cryptodata.ui.screen.detail

import android.content.res.Configuration
import android.util.Log
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
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
import dev.shorthouse.cryptodata.model.CoinDetail
import dev.shorthouse.cryptodata.ui.component.LoadingIndicator
import dev.shorthouse.cryptodata.ui.preview.CoinDetailPreviewProvider
import dev.shorthouse.cryptodata.ui.screen.detail.component.CoinDetailList
import dev.shorthouse.cryptodata.ui.screen.detail.component.CoinDetailListItem
import dev.shorthouse.cryptodata.ui.screen.detail.component.PriceChangePercentageChip
import dev.shorthouse.cryptodata.ui.theme.AppTheme

@Composable
fun DetailScreen(
    navController: NavController,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DetailScreen(
        uiState = uiState,
        onNavigateUp = { navController.navigateUp() },
        onClickChartPeriod = { viewModel.onClickChartPeriod(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    uiState: DetailUiState,
    onNavigateUp: () -> Unit,
    onClickChartPeriod: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        is DetailUiState.Loading -> LoadingIndicator()
        is DetailUiState.Error -> Text("error")
        is DetailUiState.Success -> {
            val coinDetail = uiState.coinDetail
            coinDetail?.let {
                Scaffold(
                    topBar = {
                        DetailTopBar(
                            coinName = it.name,
                            coinSymbol = it.symbol,
                            onNavigateUp = onNavigateUp
                        )
                    },
                    content = { scaffoldPadding ->
                        DetailContent(
                            coinDetail = it,
                            chartPeriodDays = uiState.chartPeriodDays,
                            onClickChartPeriod = onClickChartPeriod,
                            modifier = Modifier.padding(scaffoldPadding)
                        )
                    },
                    modifier = modifier
                )
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun DetailTopBar(
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
private fun DetailContent(
    coinDetail: CoinDetail,
    chartPeriodDays: String,
    onClickChartPeriod: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Log.d("HDS", "input chart period: $chartPeriodDays")

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        val greyscaleColorFilter =
            remember { ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0F) }) }

        AsyncImage(
            model = coinDetail.image,
            placeholder = painterResource(R.drawable.ic_launcher_background),
            contentDescription = stringResource(R.string.cd_coin_image),
            colorFilter = greyscaleColorFilter,
            modifier = Modifier
                .padding(top = 8.dp)
                .size(64.dp)
        )

        Text(
            text = coinDetail.currentPrice.formattedAmount,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(Modifier.height(4.dp))

        PriceChangePercentageChip(
            priceChangePercentage = coinDetail.periodPriceChangePercentage
        )

        Spacer(Modifier.height(32.dp))

        CoinPastPricesChart(
            coinPastPrices = coinDetail.pastPrices,
            minPrice = coinDetail.minPastPrice.amount,
            maxPrice = coinDetail.maxPastPrice.amount
        )

        Spacer(Modifier.height(24.dp))

        val chartPeriodOptions = listOf(
            "1",
            "7",
            "30",
            "365"
        )

        Row {
            chartPeriodOptions.forEach { chartPeriodOption ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .selectable(
                            selected = (chartPeriodOption == chartPeriodDays),
                            onClick = { onClickChartPeriod(chartPeriodOption) },
                            role = Role.RadioButton
                        )
                ) {
                    RadioButton(
                        selected = (chartPeriodOption == chartPeriodDays),
                        onClick = null,
                        modifier = Modifier.padding(8.dp)
                    )

                    Text(
                        text = chartPeriodOption,
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
                    text = coinDetail.minPastPrice.formattedAmount,
                    style = MaterialTheme.typography.bodyLarge
                )
                PriceChangePercentageChip(
                    priceChangePercentage = coinDetail.minPriceChangePercentage
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
                    text = coinDetail.maxPastPrice.formattedAmount,
                    style = MaterialTheme.typography.bodyLarge
                )
                PriceChangePercentageChip(
                    priceChangePercentage = coinDetail.maxPriceChangePercentage
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
@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
fun DetailScreenPreview(
    @PreviewParameter(CoinDetailPreviewProvider::class) coinDetail: CoinDetail
) {
    AppTheme {
        DetailScreen(
            uiState = DetailUiState.Success(
                coinDetail = coinDetail,
                chartPeriodDays = "7"
            ),
            onNavigateUp = {},
            onClickChartPeriod = {}
        )
    }
}
