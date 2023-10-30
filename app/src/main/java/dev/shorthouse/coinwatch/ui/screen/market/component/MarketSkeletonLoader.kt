package dev.shorthouse.coinwatch.ui.screen.market.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.shorthouse.coinwatch.ui.component.SkeletonSurface
import dev.shorthouse.coinwatch.ui.theme.AppTheme

@Composable
fun MarketSkeletonLoader(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp)
    ) {
        Spacer(Modifier.padding(1.dp))

        SkeletonSurface(
            shape = MaterialTheme.shapes.medium.copy(
                bottomStart = CornerSize(0.dp),
                bottomEnd = CornerSize(0.dp)
            ),
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
@Preview
private fun MarketSkeletonLoaderPreview() {
    AppTheme {
        MarketSkeletonLoader()
    }
}
