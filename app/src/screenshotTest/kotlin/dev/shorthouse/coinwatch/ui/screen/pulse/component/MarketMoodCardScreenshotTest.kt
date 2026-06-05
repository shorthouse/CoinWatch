package dev.shorthouse.coinwatch.ui.screen.pulse.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.tools.screenshot.PreviewTest
import dev.shorthouse.coinwatch.R
import dev.shorthouse.coinwatch.model.FearGreed
import dev.shorthouse.coinwatch.model.FearGreedMoodBand
import dev.shorthouse.coinwatch.ui.theme.AppTheme
import java.math.BigDecimal
import kotlinx.collections.immutable.persistentListOf
import androidx.compose.material3.MaterialTheme

@PreviewTest
@Preview
@Composable
fun MarketMoodCardScreenshotTest() {
    AppTheme {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(12.dp)
        ) {
            PulseSection(title = stringResource(R.string.pulse_market_mood)) {
                MarketMoodCard(
                    fearGreed = screenshotFearGreed()
                )
            }
        }
    }
}

private fun screenshotFearGreed() = FearGreed(
    value = 20,
    moodBand = FearGreedMoodBand.ExtremeFear,
    history = persistentListOf(
        BigDecimal("54"),
        BigDecimal("50"),
        BigDecimal("48"),
        BigDecimal("52"),
        BigDecimal("46"),
        BigDecimal("41"),
        BigDecimal("39"),
        BigDecimal("36"),
        BigDecimal("34"),
        BigDecimal("38"),
        BigDecimal("31"),
        BigDecimal("30"),
        BigDecimal("25"),
        BigDecimal("20")
    )
)
