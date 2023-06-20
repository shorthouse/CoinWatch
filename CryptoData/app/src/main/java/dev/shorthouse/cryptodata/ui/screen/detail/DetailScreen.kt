package dev.shorthouse.cryptodata.ui.screen.detail

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import dev.shorthouse.cryptodata.R
import dev.shorthouse.cryptodata.model.CoinDetail
import dev.shorthouse.cryptodata.ui.component.LoadingIndicator
import dev.shorthouse.cryptodata.ui.theme.AppTheme
import dev.shorthouse.cryptodata.ui.theme.PositiveGreen
import dev.shorthouse.cryptodata.ui.theme.PositiveGreenBackground

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
private fun DetailContent(
    coinDetail: CoinDetail,
    modifier: Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        AsyncImage(
            model = coinDetail.image,
            placeholder = painterResource(R.drawable.ic_launcher_background),
            contentDescription = "Coin image"
        )

        Text(
            text = stringResource(
                id = R.string.coin_current_price,
                coinDetail.currentPrice
            ),
            style = MaterialTheme.typography.headlineSmall
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(
                    color = PositiveGreenBackground,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.ExpandLess,
                contentDescription = null,
                tint = PositiveGreen
            )
            Text(
                text = stringResource(
                    id = R.string.coin_price_change,
                    coinDetail.priceChangePercentage
                ),
                color = PositiveGreen
            )
        }


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
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        },
        modifier = modifier
    )
}

@Composable
@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
fun DetailScreenPreview() {
    AppTheme {
        DetailScreen(
            coinDetail = CoinDetail(
                id = "ethereum",
                symbol = "ETH",
                name = "Ethereum",
                image = "",
                currentPrice = 1432.27,
                priceChangePercentage = 4.497324,
                description = "Ethereum is a global, open-source platform for decentralized applications. In other words, the vision is to create a world computer that anyone can build applications in a decentralized manner; while all states and data are distributed and publicly accessible. Ethereum supports smart contracts in which developers can write code in order to program digital value. Examples of decentralized apps (dapps) that are built on Ethereum includes tokens, non-fungible tokens, decentralized finance apps, lending protocol, decentralized exchanges, and much more.\r\n\r\nOn Ethereum, all transactions and smart contract executions require a small fee to be paid. This fee is called Gas. In technical terms, Gas refers to the unit of measure on the amount of computational effort required to execute an operation or a smart contract. The more complex the execution operation is, the more gas is required to fulfill that operation. Gas fees are paid entirely in Ether (ETH), which is the native coin of the blockchain. The price of gas can fluctuate from time to time depending on the network demand.",
                genesisDate = "2015-07-30",
                homepageLink = "https://ethereum.org",
                blockchainLink = "https://etherscan.io/",
                subredditLink = "https://www.reddit.com/r/ethereum"
            ),
            isLoading = false,
            error = null,
            onNavigateUp = {}
        )
    }
}
