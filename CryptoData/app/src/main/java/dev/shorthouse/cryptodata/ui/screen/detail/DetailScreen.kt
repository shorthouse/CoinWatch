package dev.shorthouse.cryptodata.ui.screen.detail

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
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
import dev.shorthouse.cryptodata.ui.screen.detail.component.CoinDetailListItem
import dev.shorthouse.cryptodata.ui.screen.detail.component.PriceChangePercentageChip
import dev.shorthouse.cryptodata.ui.screen.detail.component.rememberChartMarker
import dev.shorthouse.cryptodata.ui.theme.AppTheme

@Composable
fun DetailScreen(
    navController: NavController,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    uiState.coinDetail?.let {
        DetailScreen(
            coinDetail = it,
            isLoading = uiState.isLoading,
            error = uiState.error,
            onNavigateUp = { navController.navigateUp() }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    coinDetail: CoinDetail,
    isLoading: Boolean,
    error: String?,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (isLoading) {
        LoadingIndicator()
    } else if (!error.isNullOrBlank()) {
        Text(text = error)
    } else {
        Scaffold(
            topBar = {
                DetailTopBar(
                    coinDetail = coinDetail,
                    onNavigateUp = onNavigateUp
                )
            },
            content = { scaffoldPadding ->
                DetailContent(
                    coinDetail = coinDetail,
                    modifier = Modifier.padding(scaffoldPadding)
                )
            },
            modifier = modifier
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun DetailTopBar(
    onNavigateUp: () -> Unit,
    coinDetail: CoinDetail,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        navigationIcon = {
            IconButton(onClick = onNavigateUp) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        title = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = coinDetail.name,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = coinDetail.symbol,
                    style = MaterialTheme.typography.labelLarge.copy(
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
    modifier: Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        val greyscaleColorFilter =
            remember { ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0F) }) }

        AsyncImage(
            model = coinDetail.image,
            placeholder = painterResource(R.drawable.ic_launcher_background),
            contentDescription = null,
            colorFilter = greyscaleColorFilter,
            modifier = Modifier.size(60.dp)
        )

        Text(
            text = stringResource(
                id = R.string.currency_format_decimal,
                coinDetail.currentPrice
            ),
            style = MaterialTheme.typography.headlineSmall
        )

        PriceChangePercentageChip(
            priceChangePercentage = coinDetail.priceChangePercentage24h
        )

        val chartEntries = coinDetail.historicalPrices7d.mapIndexed { index, historicalPrice ->
            entryOf(x = index, y = historicalPrice)
        }

        Chart(
            chart = lineChart(
                axisValuesOverrider = AxisValuesOverrider.fixed(
                    minY = coinDetail.historicalPrices7d.min().toFloat(),
                    maxY = coinDetail.historicalPrices7d.max().toFloat()
                )
            ),
            model = entryModelOf(chartEntries),
            chartScrollSpec = rememberChartScrollSpec(
                isScrollEnabled = false
            ),
            marker = rememberChartMarker()
        )

        Text(
            text = "Market Data",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.fillMaxWidth()
        )

        Surface(
            shadowElevation = 4.dp,
            tonalElevation = 0.dp,
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(12.dp),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(
                    horizontal = 12.dp,
                    vertical = 12.dp
                )
            ) {
                CoinDetailListItem(
                    header = "Market Cap Rank",
                    value = coinDetail.marketCapRank.toString()
                )
                CoinDetailListItem(
                    header = "Market Cap",
                    value = stringResource(
                        id = R.string.currency_format_integer,
                        coinDetail.marketCap
                    )
                )
                CoinDetailListItem(
                    header = "Total Supply",
                    value = coinDetail.totalSupply.toString()
                )
            }
        }

        Text(
            text = "Historic Data",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.fillMaxWidth()
        )

        Surface(
            shadowElevation = 4.dp,
            tonalElevation = 0.dp,
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(12.dp),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                )
            ) {
                CoinDetailListItem(
                    header = "All Time Low",
                    value = stringResource(
                        id = R.string.currency_format_decimal,
                        coinDetail.allTimeLow
                    )
                )
                CoinDetailListItem(
                    header = "All Time High",
                    value = stringResource(
                        id = R.string.currency_format_decimal,
                        coinDetail.allTimeHigh
                    )
                )
                CoinDetailListItem(
                    header = "All Time Low Date",
                    value = coinDetail.allTimeLowDate
                )
                CoinDetailListItem(
                    header = "All Time High Date",
                    value = coinDetail.allTimeHighDate
                )
                CoinDetailListItem(
                    header = "Genesis Date",
                    value = coinDetail.genesisDate
                )
            }
        }
    }
}

@Composable
@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
fun DetailScreenPreview() {
    AppTheme {
        DetailScreen(
            coinDetail = CoinDetail(
                id = "ethereum",
                name = "Ethereum",
                symbol = "ETH",
                image = "",
                currentPrice = 1432.27,
                priceChangePercentage24h = 4.497324,
                historicalPrices7d = listOf(
                    1642.7485409659523,
                    1637.7870409655195,
                    1635.7994218934289,
                    1637.0149957694669,
                    1632.457370596918,
                    1633.3078700057745,
                    1640.076430157162,
                    1638.610091542772,
                    1645.470037101135,
                    1643.5030701670662,
                    1640.5676033552272,
                    1640.414772963638,
                    1641.3836170135767,
                    1660.2067178098546,
                    1665.732643385012,
                    1660.3288181644286,
                    1665.6463321439203,
                    1664.108062844492,
                    1665.709864608598,
                    1661.8991054881817,
                    1661.16807608237,
                    1662.536619432034,
                    1665.5827681665155,
                    1666.2654218165064,
                    1669.8232345854517,
                    1671.3399654538364,
                    1669.8668062990755,
                    1670.6205880576122,
                    1668.9675695424605,
                    1668.8623392191507,
                    1664.5828123367094,
                    1665.9581998624042,
                    1664.5329682867796,
                    1656.1522650238362,
                    1670.9107847157836,
                    1685.5854219765688,
                    1693.6861430893418,
                    1718.0954361982128,
                    1719.5604705203914,
                    1724.4185812928981,
                    1717.2150995526192,
                    1718.3376076916852,
                    1716.3772810928574,
                    1715.3728699191638,
                    1716.4572936053246,
                    1719.3857674133008,
                    1717.9402789420633,
                    1722.9172507600233,
                    1755.9681286796035,
                    1749.1109911092306,
                    1742.5763751255242,
                    1742.8789222747218,
                    1743.36283680448,
                    1742.949443525001,
                    1739.6798827197129,
                    1736.6501478523637,
                    1739.881094628734,
                    1734.350551829202,
                    1727.3088109899484,
                    1728.3542120522309,
                    1724.0508933235844,
                    1730.0350154104376,
                    1726.8724968432164,
                    1727.705836034807,
                    1728.4853371798893,
                    1729.9344829511347,
                    1726.3728097349979,
                    1722.9236795626043,
                    1726.6732691903158,
                    1724.7570617148756,
                    1728.4138754891442,
                    1729.2013282798684,
                    1728.1994031874724,
                    1727.9836315909324,
                    1729.959915188853,
                    1727.7994200404296,
                    1732.0418513347486,
                    1730.22164381322,
                    1733.160509022454,
                    1734.1439429708703,
                    1734.3126583675096,
                    1739.6185750073641,
                    1737.763846276208,
                    1739.520428326312,
                    1742.9818604051345,
                    1738.3566639120393,
                    1740.769826692253,
                    1729.880664769764,
                    1725.044044449943,
                    1725.1041210491662,
                    1719.2731615068672,
                    1721.8453926102754,
                    1725.6130551321844,
                    1726.424811464582,
                    1724.026785980998,
                    1721.3894923131418,
                    1726.3177627661914,
                    1724.2789467819323,
                    1724.9908623712372,
                    1721.9714080474478,
                    1723.0291699955617,
                    1723.9833342077068,
                    1724.4988435604077,
                    1730.302583742191,
                    1727.6725145045236,
                    1719.2805941959675,
                    1720.2668434475615,
                    1716.1782365564127,
                    1728.9511340664908,
                    1732.2176060174259,
                    1722.2754388367487,
                    1730.7013829884743,
                    1729.7263051323084,
                    1733.5642054505356,
                    1734.7931034426633,
                    1740.0751986017044,
                    1733.488368392057,
                    1736.2569289281414,
                    1731.8887141130901,
                    1730.013138767798,
                    1728.3393414183436,
                    1728.5471915023204,
                    1729.4026081424547,
                    1725.1581259095333,
                    1729.7113856545423,
                    1730.0485899979537,
                    1729.5889423673468,
                    1732.0470306863247,
                    1727.890706538856,
                    1719.391401434068,
                    1732.6595010894928,
                    1744.4351278772606,
                    1755.8932585692048,
                    1783.5800510109596,
                    1778.9565086470993,
                    1783.3341335725602,
                    1775.72209382429,
                    1785.5516986015066,
                    1792.1166967549614,
                    1798.6598182511227,
                    1806.8058516468968,
                    1808.9351105526357,
                    1810.8382377255434,
                    1814.5759723810686,
                    1820.168830697634,
                    1810.8389938225541,
                    1812.0139012131367,
                    1809.2086258540535,
                    1813.8303318677208,
                    1812.7525198840478,
                    1829.0275498558847,
                    1816.9299836822468,
                    1837.5333766428478,
                    1839.5782541511744,
                    1850.3618002671797,
                    1882.710832734731,
                    1870.3198269713882,
                    1877.6643584682902,
                    1881.2746791916318,
                    1879.3788322613643,
                    1878.25292653696,
                    1885.9686606895586,
                    1892.899571916819,
                    1913.1842105832884,
                    1916.7049526229903,
                    1922.831648666678,
                    1916.261852379821,
                    1917.3066847022285
                ),
                dailyHigh = 1539.23,
                dailyHighChangePercentage = -4.23,
                dailyLow = 1419.21,
                dailyLowChangePercentage = 2.13,
                marketCapRank = 2,
                marketCap = 34934943,
                totalSupply = 414424.21,
                allTimeLow = 0.79,
                allTimeHigh = 3260.39,
                allTimeLowDate = "10th October 2015",
                allTimeHighDate = "22nd May 2021",
                genesisDate = "30th July 2015",
            ),
            isLoading = false,
            error = null,
            onNavigateUp = {}
        )
    }
}
