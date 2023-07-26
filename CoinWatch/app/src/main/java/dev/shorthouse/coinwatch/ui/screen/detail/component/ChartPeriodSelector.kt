package dev.shorthouse.coinwatch.ui.screen.detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.shorthouse.coinwatch.ui.model.ChartPeriod
import dev.shorthouse.coinwatch.ui.theme.AppTheme

@Composable
fun ChartPeriodSelector(
    selectedChartPeriod: ChartPeriod,
    onChartPeriodSelected: (ChartPeriod) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) {
        val chartPeriods = remember { ChartPeriod.values() }

        Row(modifier = Modifier.padding(4.dp)) {
            chartPeriods.forEach { chartPeriod ->
                val backgroundColor = if (chartPeriod == selectedChartPeriod) {
                    MaterialTheme.colorScheme.surface
                } else {
                    MaterialTheme.colorScheme.background
                }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .clip(MaterialTheme.shapes.medium)
                        .background(backgroundColor)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = { onChartPeriodSelected(chartPeriod) }
                        )
                ) {
                    Text(
                        text = stringResource(chartPeriod.shortNameId),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(vertical = 8.dp)

                    )
                }
            }
        }
    }
}

@Composable
@Preview
private fun SegmentedButtonPreview() {
    AppTheme {
        var selectedChartPeriod by remember { mutableStateOf(ChartPeriod.Week) }

        ChartPeriodSelector(
            selectedChartPeriod = ChartPeriod.Day,
            onChartPeriodSelected = { selectedChartPeriod = it }
        )
    }
}
