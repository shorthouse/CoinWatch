package dev.shorthouse.coinwatch.ui.screen.details.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.StarOutline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.shorthouse.coinwatch.R
import dev.shorthouse.coinwatch.ui.component.SkeletonSurface
import dev.shorthouse.coinwatch.ui.theme.AppTheme

@Composable
fun CoinDetailsSkeletonLoader(modifier: Modifier = Modifier) {
    Scaffold(
        topBar = {
            DetailsSkeletonTopBar()
        },
        content = { scaffoldPadding ->
            DetailsSkeletonContent(modifier = Modifier.padding(scaffoldPadding))
        },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailsSkeletonTopBar(modifier: Modifier = Modifier) {
    LargeTopAppBar(
        navigationIcon = {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = stringResource(R.string.cd_top_bar_back)
                )
            }
        },
        title = {},
        actions = {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Rounded.StarOutline,
                    contentDescription = stringResource(R.string.cd_top_bar_favourite),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        modifier = modifier
    )
}

@Composable
private fun DetailsSkeletonContent(modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(horizontal = 12.dp)) {
        SkeletonSurface(
            modifier = Modifier
                .fillMaxWidth()
                .height(374.dp)
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.title_chart_range),
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(Modifier.height(8.dp))

        SkeletonSurface(
            modifier = Modifier
                .fillMaxWidth()
                .height(91.dp)
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.card_header_market_stats),
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(Modifier.height(8.dp))

        SkeletonSurface(
            shape = MaterialTheme.shapes.medium.copy(
                bottomStart = CornerSize(0.dp),
                bottomEnd = CornerSize(0.dp)
            ),
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview
@Composable
private fun CoinDetailSkeletonLoaderPreview() {
    AppTheme {
        CoinDetailsSkeletonLoader()
    }
}
