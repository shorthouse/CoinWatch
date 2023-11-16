package dev.shorthouse.coinwatch.ui.screen.details.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.shorthouse.coinwatch.R
import dev.shorthouse.coinwatch.ui.component.SkeletonSurface
import dev.shorthouse.coinwatch.ui.theme.AppTheme

@Composable
fun DetailsSkeletonLoader(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp)
    ) {
        SkeletonSurface(
            modifier = Modifier
                .fillMaxWidth()
                .height(374.dp)
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.title_chart_range),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
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
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
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

@Composable
@Preview(showBackground = true)
private fun DetailsSkeletonLoaderPreview() {
    AppTheme {
        DetailsSkeletonLoader()
    }
}
