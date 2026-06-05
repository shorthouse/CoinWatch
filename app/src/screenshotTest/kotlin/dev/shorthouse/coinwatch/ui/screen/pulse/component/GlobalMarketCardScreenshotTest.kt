package dev.shorthouse.coinwatch.ui.screen.pulse.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.tools.screenshot.PreviewTest
import dev.shorthouse.coinwatch.R
import dev.shorthouse.coinwatch.data.source.local.datastore.global.Currency
import dev.shorthouse.coinwatch.model.GlobalMarket
import dev.shorthouse.coinwatch.model.Price
import dev.shorthouse.coinwatch.ui.theme.AppTheme
import java.math.BigDecimal

@PreviewTest
@Preview
@Composable
fun GlobalMarketCardScreenshotTest() {
    AppTheme {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(12.dp)
        ) {
            PulseSection(title = stringResource(R.string.pulse_global_market)) {
                GlobalMarketCard(
                    globalMarket = screenshotGlobalMarket()
                )
            }
        }
    }
}

private fun screenshotGlobalMarket() = GlobalMarket(
    totalMarketCap = Price("2410000000000", Currency.USD),
    volume24h = Price("98200000000", Currency.USD),
    btcDominancePercentage = BigDecimal("54.2"),
    coinsUp24h = 2841,
    coinsDown24h = 1893
)
