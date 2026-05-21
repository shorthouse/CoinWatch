package dev.shorthouse.coinwatch.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.tools.screenshot.PreviewTest
import dev.shorthouse.coinwatch.data.source.local.datastore.common.CoinSort
import dev.shorthouse.coinwatch.ui.theme.AppTheme

@OptIn(ExperimentalLayoutApi::class)
@PreviewTest
@Preview(showBackground = true)
@Composable
fun CoinSortChipScreenshotTest() {
    AppTheme {
        val coinSorts = CoinSort.entries.toTypedArray()

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(12.dp)
        ) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                coinSorts.forEach { coinSort ->
                    CoinSortChip(
                        coinSort = coinSort,
                        selected = false,
                        onClick = {}
                    )
                }
            }

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                coinSorts.forEach { coinSort ->
                    CoinSortChip(
                        coinSort = coinSort,
                        selected = true,
                        onClick = {}
                    )
                }
            }
        }
    }
}
