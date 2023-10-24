package dev.shorthouse.coinwatch.ui.screen.list.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.shorthouse.coinwatch.ui.component.SkeletonSurface
import dev.shorthouse.coinwatch.ui.theme.AppTheme

@Composable
fun CoinListSkeletonLoader(modifier: Modifier = Modifier) {
    Scaffold(
        topBar = {
            SkeletonTopAppBar()
        },
        content = { scaffoldPadding ->
            SkeletonContent(
                modifier = Modifier.padding(scaffoldPadding)
            )
        },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SkeletonTopAppBar(modifier: Modifier = Modifier) {
    TopAppBar(
        title = {},
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        modifier = modifier
    )
}

@Composable
private fun SkeletonContent(modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(start = 12.dp, top = 12.dp)) {
        SkeletonSurface(
            shape = MaterialTheme.shapes.medium.copy(
                bottomStart = CornerSize(0.dp),
                bottomEnd = CornerSize(0.dp)
            ),
            modifier = Modifier
                .fillMaxSize()
                .padding(end = 12.dp)
        )
    }
}

@Composable
@Preview
fun CoinListSkeletonLoaderPreview() {
    AppTheme {
        CoinListSkeletonLoader()
    }
}
