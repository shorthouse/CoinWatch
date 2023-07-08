package dev.shorthouse.cryptodata.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.shorthouse.cryptodata.ui.theme.AppTheme
import kotlin.math.ceil

@Composable
fun PriceChart(
    prices: List<Double>,
    chartColor: Color,
    modifier: Modifier = Modifier
) {
    val spacing = 100f
    val transparentGraphColor = remember {
        chartColor.copy(alpha = 0.5f)
    }

    val minPrice = remember(prices) {
        prices.minOrNull() ?: 0.0
    }

    val maxPrice = remember(prices) {
        ceil(prices.maxOrNull() ?: 0.0)
    }

    Canvas(modifier = modifier) {
        val spacePerHour = (size.width - spacing) / prices.size
        var lastX = 0f

        val strokePath = Path().apply {
            val height = size.height
            for (i in prices.indices) {
                val price = prices[i]
                val nextPrice = prices.getOrNull(i + 1) ?: prices.last()

                val leftRatio = (price - minPrice) / (maxPrice - minPrice)
                val rightRatio = (nextPrice - minPrice) / (maxPrice - minPrice)

                val x1 = spacing + i * spacePerHour
                val y1 = height - spacing - (leftRatio * height).toFloat()
                val x2 = spacing + (i + 1) * spacePerHour
                val y2 = height - spacing - (rightRatio * height).toFloat()

                if (i == 0) {
                    moveTo(x1, y1)
                }

                lastX = (x1 + x2) / 2f

                quadraticBezierTo(
                    x1,
                    y1,
                    lastX,
                    (y1 + y2) / 2f
                )
            }
        }

        val fillPath = android.graphics.Path(strokePath.asAndroidPath())
            .asComposePath()
            .apply {
                lineTo(lastX, size.height - spacing)
                lineTo(spacing, size.height - spacing)
                close()
            }

        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(
                    transparentGraphColor,
                    Color.Transparent
                ),
                endY = size.height - spacing
            )
        )

        drawPath(
            path = strokePath,
            color = chartColor,
            style = Stroke(
                width = 3.dp.toPx(),
                cap = StrokeCap.Round
            )
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun PriceChartPreview() {
    AppTheme {
        PriceChart(
            prices = listOf(0.0, 10.0, 50.0, 70.0, 40.0),
            chartColor = Color.Green,
            modifier = Modifier.fillMaxSize()
        )
    }
}
