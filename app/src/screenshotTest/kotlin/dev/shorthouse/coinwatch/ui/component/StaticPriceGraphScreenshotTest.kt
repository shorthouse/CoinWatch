package dev.shorthouse.coinwatch.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.tools.screenshot.PreviewTest
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.ui.theme.AppTheme
import kotlinx.collections.immutable.persistentListOf
import java.math.BigDecimal

@PreviewTest
@Preview
@Composable
fun StaticPriceGraphScreenshotTest() {
    AppTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            StaticPriceGraph(
                prices = persistentListOf(
                    BigDecimal("100.00"),
                    BigDecimal("115.00"),
                    BigDecimal("110.00"),
                    BigDecimal("125.00"),
                    BigDecimal("140.00"),
                ),
                priceChangePercentage = Percentage("5.12"),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
            )

            StaticPriceGraph(
                prices = persistentListOf(
                    BigDecimal("140.00"),
                    BigDecimal("130.00"),
                    BigDecimal("135.00"),
                    BigDecimal("115.00"),
                    BigDecimal("100.00"),
                ),
                priceChangePercentage = Percentage("-4.28"),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
            )

            StaticPriceGraph(
                prices = persistentListOf(
                    BigDecimal("100.00"),
                    BigDecimal("100.00"),
                    BigDecimal("100.00"),
                    BigDecimal("100.00"),
                ),
                priceChangePercentage = Percentage("0"),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
            )
        }
    }
}
