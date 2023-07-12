package dev.shorthouse.cryptodata.ui.screen.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import dev.shorthouse.cryptodata.R
import dev.shorthouse.cryptodata.model.CoinChart
import dev.shorthouse.cryptodata.model.CoinDetail
import dev.shorthouse.cryptodata.ui.component.LoadingIndicator
import dev.shorthouse.cryptodata.ui.previewdata.CoinDetailUiStatePreviewProvider
import dev.shorthouse.cryptodata.ui.screen.detail.component.CoinChartCard
import dev.shorthouse.cryptodata.ui.screen.detail.component.CoinDetailCard
import dev.shorthouse.cryptodata.ui.screen.detail.component.CoinTitleCard
import dev.shorthouse.cryptodata.ui.theme.AppTheme
import kotlin.time.Duration

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
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        },
                        title = {},
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    )
                },
                content = { scaffoldPadding ->
                    Box {
                        Surface(
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .padding(scaffoldPadding)
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = MaterialTheme.shapes.medium.copy(
                                topStart = CornerSize(0.dp),
                                topEnd = CornerSize(0.dp)
                            ),
                            content = {}
                        )
                        CoinDetailContent(
                            coinDetail = uiState.coinDetail,
                            coinChart = uiState.coinChart,
                            chartPeriod = uiState.chartPeriod,
                            onClickChartPeriod = onClickChartPeriod,
                            modifier = Modifier.padding(scaffoldPadding)
                        )
                    }
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
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, bottom = 24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        CoinTitleCard(
            coinDetail = coinDetail
        )

        CoinChartCard(
            coinDetail = coinDetail,
            coinChart = coinChart,
            chartPeriod = chartPeriod,
            onClickChartPeriod = onClickChartPeriod
        )

        CoinDetailCard(
            coinDetail = coinDetail
        )
    }
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
