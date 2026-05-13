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
import dev.shorthouse.coinwatch.model.PriceEntry
import dev.shorthouse.coinwatch.ui.theme.AppTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.math.BigDecimal

@PreviewTest
@Preview(widthDp = 360, heightDp = 560)
@Composable
fun ScrubPriceGraphScreenshotTest() {
    AppTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            ScrubPriceGraph(
                priceHistory = positivePriceHistory(),
                priceChangePercentage = Percentage("0.42"),
                isGraphAnimated = false,
                onScrub = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
            )

            ScrubPriceGraph(
                priceHistory = negativePriceHistory(),
                priceChangePercentage = Percentage("-4.28"),
                isGraphAnimated = false,
                onScrub = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
            )

            ScrubPriceGraph(
                priceHistory = neutralPriceHistory(),
                priceChangePercentage = Percentage("0"),
                isGraphAnimated = false,
                onScrub = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
            )
        }
    }
}

private fun positivePriceHistory(): ImmutableList<PriceEntry> = persistentListOf(
    PriceEntry(BigDecimal("1650.19"), 1700000000L, "14 Nov"),
    PriceEntry(BigDecimal("1650.71"), 1700003600L, "14 Nov"),
    PriceEntry(BigDecimal("1670.94"), 1700007200L, "15 Nov"),
    PriceEntry(BigDecimal("1680.44"), 1700010800L, "15 Nov"),
    PriceEntry(BigDecimal("1743.98"), 1700014400L, "15 Nov"),
    PriceEntry(BigDecimal("1740.25"), 1700018000L, "15 Nov"),
    PriceEntry(BigDecimal("1737.53"), 1700021600L, "16 Nov"),
    PriceEntry(BigDecimal("1730.56"), 1700025200L, "16 Nov"),
    PriceEntry(BigDecimal("1738.12"), 1700028800L, "16 Nov"),
    PriceEntry(BigDecimal("1736.10"), 1700032400L, "16 Nov"),
)

private fun negativePriceHistory(): ImmutableList<PriceEntry> = persistentListOf(
    PriceEntry(BigDecimal("140.00"), 1700000000L, "14 Nov"),
    PriceEntry(BigDecimal("130.00"), 1700003600L, "14 Nov"),
    PriceEntry(BigDecimal("135.00"), 1700007200L, "15 Nov"),
    PriceEntry(BigDecimal("115.00"), 1700010800L, "15 Nov"),
    PriceEntry(BigDecimal("100.00"), 1700014400L, "15 Nov"),
)

private fun neutralPriceHistory(): ImmutableList<PriceEntry> = persistentListOf(
    PriceEntry(BigDecimal("100.00"), 1700000000L, "14 Nov"),
    PriceEntry(BigDecimal("100.00"), 1700003600L, "14 Nov"),
    PriceEntry(BigDecimal("100.00"), 1700007200L, "15 Nov"),
    PriceEntry(BigDecimal("100.00"), 1700010800L, "15 Nov"),
)
