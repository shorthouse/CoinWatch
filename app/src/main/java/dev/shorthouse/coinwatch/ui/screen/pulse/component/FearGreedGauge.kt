package dev.shorthouse.coinwatch.ui.screen.pulse.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import dev.shorthouse.coinwatch.ui.preview.AppPreviewWrapper
import dev.shorthouse.coinwatch.ui.theme.FearAmber
import dev.shorthouse.coinwatch.ui.theme.GreedLightGreen
import dev.shorthouse.coinwatch.ui.theme.NegativeRed
import dev.shorthouse.coinwatch.ui.theme.PositiveGreen
import dev.shorthouse.coinwatch.ui.theme.ZeroWhite

private val GaugeHeight = 22.dp
private val TrackHeight = 8.dp
private val MarkerHaloWidth = 10.dp
private val MarkerWidth = 4.dp

@Composable
fun FearGreedGauge(
    value: Int,
    modifier: Modifier = Modifier,
) {
    val trackBrush = remember {
        Brush.horizontalGradient(
            0.125f to NegativeRed,
            0.36f to FearAmber,
            0.505f to ZeroWhite,
            0.65f to GreedLightGreen,
            0.88f to PositiveGreen
        )
    }

    val markerColor = MaterialTheme.colorScheme.onSurface
    val markerHaloColor = MaterialTheme.colorScheme.surface

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(GaugeHeight)
    ) {
        val trackHeight = TrackHeight.toPx()

        drawRoundRect(
            brush = trackBrush,
            topLeft = Offset(x = 0f, y = (size.height - trackHeight) / 2),
            size = Size(width = size.width, height = trackHeight),
            cornerRadius = CornerRadius(trackHeight / 2)
        )

        val halfHalo = MarkerHaloWidth.toPx() / 2
        val progress = value.coerceIn(0, 100) / 100f
        val maxCenterX = (size.width - halfHalo).coerceAtLeast(halfHalo)
        val markerCenterX = (size.width * progress).coerceIn(halfHalo, maxCenterX)

        drawMarker(centerX = markerCenterX, width = MarkerHaloWidth.toPx(), color = markerHaloColor)
        drawMarker(centerX = markerCenterX, width = MarkerWidth.toPx(), color = markerColor)
    }
}

private fun DrawScope.drawMarker(centerX: Float, width: Float, color: Color) {
    drawRoundRect(
        color = color,
        topLeft = Offset(x = centerX - width / 2, y = 0f),
        size = Size(width = width, height = size.height),
        cornerRadius = CornerRadius(width / 2)
    )
}

@Preview
@PreviewWrapper(wrapper = AppPreviewWrapper::class)
@Composable
private fun FearGreedGaugePreview() {
    FearGreedGauge(value = 20)
}
