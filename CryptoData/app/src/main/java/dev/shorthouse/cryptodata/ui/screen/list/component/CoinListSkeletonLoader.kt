package dev.shorthouse.cryptodata.ui.screen.list.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.shorthouse.cryptodata.R
import dev.shorthouse.cryptodata.ui.theme.AppTheme

@Composable
fun CoinListSkeletonLoader(
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            SkeletonTopAppBar()
        },
        content = { scaffoldPadding ->
            Column(
                modifier = modifier
                    .padding(scaffoldPadding)
                    .padding(start = 12.dp, top = 12.dp, end = 12.dp)
            ) {
                SkeletonFavouriteCoins()

                SkeletonCoins()
            }
        },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SkeletonTopAppBar(
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 12.dp)
            ) {
                CoinGeckoAttribution()
            }
        },
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            scrolledContainerColor = MaterialTheme.colorScheme.background
        ),
        modifier = modifier
    )
}

@Composable
private fun SkeletonFavouriteCoins(
    modifier: Modifier = Modifier
) {
    CoinListTitle(
        text = stringResource(R.string.header_favourites)
    )

    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(bottom = 24.dp)
    ) {
        repeat(4) {
            Box(
                modifier = modifier
                    .size(width = 140.dp, height = 200.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.surface)
            )
        }
    }
}

@Composable
private fun SkeletonCoins() {
    CoinListTitle(
        text = stringResource(R.string.header_coins)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(
                MaterialTheme.shapes.medium.copy(
                    bottomStart = CornerSize(0.dp),
                    bottomEnd = CornerSize(0.dp)
                )
            )
            .background(MaterialTheme.colorScheme.surface)
    )
}

@Preview
@Composable
fun CoinListSkeletonLoaderPreview() {
    AppTheme {
        CoinListSkeletonLoader()
    }
}
