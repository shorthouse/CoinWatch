package dev.shorthouse.cryptodata.ui.screen.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import dev.shorthouse.cryptodata.ui.screen.detail.component.CoinDetailList
import dev.shorthouse.cryptodata.ui.screen.detail.component.CoinDetailListItem
import dev.shorthouse.cryptodata.ui.screen.detail.component.PriceChangePercentageChip

@Composable
fun DetailScreen(
    navController: NavController,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DetailScreen(
        uiState = uiState,
        onNavigateUp = { navController.navigateUp() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    uiState: DetailUiState,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        is DetailUiState.Loading -> LoadingIndicator()
        is DetailUiState.Error -> Text("error")
        is DetailUiState.Success -> {
            val coinDetail = uiState.data
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
    modifier: Modifier = Modifier
) {
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
            text = coinDetail.currentPrice,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(Modifier.height(4.dp))

        PriceChangePercentageChip(
            priceChangePercentage = 2.0
        )

        CoinPastPricesChart(
            coinPastPrices = coinDetail.pastPrices,
            minPrice = coinDetail.minPrice,
            maxPrice = coinDetail.maxPrice
        )

        CoinDetailList(
            title = stringResource(R.string.list_header_market_data),
            items = listOf(
                CoinDetailListItem(
                    name = stringResource(R.string.list_item_market_cap_rank),
                    value = coinDetail.marketCapRank.toString()
                ),
                CoinDetailListItem(
                    name = stringResource(R.string.list_item_market_cap),
                    value = coinDetail.marketCap
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
                    value = coinDetail.allTimeLow
                ),
                CoinDetailListItem(
                    name = stringResource(R.string.list_item_ath),
                    value = coinDetail.allTimeHigh
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
    maxPrice: Double
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
        modifier = Modifier.padding(vertical = 32.dp)
    )
}

// @Composable
// @Preview(name = "Light Mode", showBackground = true)
// @Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
// fun DetailScreenPreview(
//    @PreviewParameter(CoinDetailPreviewProvider::class) coinDetail: CoinDetailOld
// ) {
//    AppTheme {
//        DetailScreen(
//            coinDetail = coinDetail,
//            coinPastPrices = CoinPastPrices(
//                prices = emptyList(),
//                minPrice = "0",
//                maxPrice = "0",
//                minPriceChangePercentage = 0.1,
//                maxPriceChangePercentage = -0.2
//            ),
//            isLoading = false,
//            error = null,
//            onNavigateUp = {},
//            isCoinPastPricesLoading = false
//        )
//    }
// }
