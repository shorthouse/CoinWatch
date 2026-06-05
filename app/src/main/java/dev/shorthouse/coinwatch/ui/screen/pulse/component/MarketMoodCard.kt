package dev.shorthouse.coinwatch.ui.screen.pulse.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import dev.shorthouse.coinwatch.R
import dev.shorthouse.coinwatch.model.FearGreed
import dev.shorthouse.coinwatch.model.FearGreedMoodBand
import dev.shorthouse.coinwatch.ui.component.StaticLineGraph
import dev.shorthouse.coinwatch.ui.preview.AppPreviewWrapper
import dev.shorthouse.coinwatch.ui.theme.FearAmber
import dev.shorthouse.coinwatch.ui.theme.GreedLightGreen
import dev.shorthouse.coinwatch.ui.theme.NegativeRed
import dev.shorthouse.coinwatch.ui.theme.PositiveGreen
import dev.shorthouse.coinwatch.ui.theme.ZeroWhite
import kotlinx.collections.immutable.persistentListOf
import java.math.BigDecimal

@Composable
fun MarketMoodCard(
    fearGreed: FearGreed,
    modifier: Modifier = Modifier,
) {
    val moodBand = fearGreed.moodBand
    val moodColor = when (moodBand) {
        FearGreedMoodBand.ExtremeFear -> NegativeRed
        FearGreedMoodBand.Fear -> FearAmber
        FearGreedMoodBand.Neutral -> ZeroWhite
        FearGreedMoodBand.Greed -> GreedLightGreen
        FearGreedMoodBand.ExtremeGreed -> PositiveGreen
    }
    val moodLabel = when (moodBand) {
        FearGreedMoodBand.ExtremeFear -> stringResource(R.string.pulse_mood_extreme_fear)
        FearGreedMoodBand.Fear -> stringResource(R.string.pulse_mood_fear)
        FearGreedMoodBand.Neutral -> stringResource(R.string.pulse_mood_neutral)
        FearGreedMoodBand.Greed -> stringResource(R.string.pulse_mood_greed)
        FearGreedMoodBand.ExtremeGreed -> stringResource(R.string.pulse_mood_extreme_greed)
    }

    Surface(
        shape = MaterialTheme.shapes.large,
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = fearGreed.value.toString(),
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Column {
                    Text(
                        text = moodLabel,
                        style = MaterialTheme.typography.titleLarge,
                        color = moodColor
                    )

                    Text(
                        text = stringResource(R.string.pulse_market_mood_out_of_100),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            FearGreedGauge(value = fearGreed.value)

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.pulse_mood_extreme_fear),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = stringResource(R.string.pulse_mood_neutral),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = stringResource(R.string.pulse_mood_extreme_greed),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.primaryContainer)

            Text(
                text = stringResource(R.string.pulse_market_mood_past_30_days),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            StaticLineGraph(
                values = fearGreed.history,
                lineColor = moodColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            )
        }
    }
}

@Preview
@PreviewWrapper(wrapper = AppPreviewWrapper::class)
@Composable
private fun MarketMoodCardPreview() {
    MarketMoodCard(
        fearGreed = FearGreed(
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
    )
}
