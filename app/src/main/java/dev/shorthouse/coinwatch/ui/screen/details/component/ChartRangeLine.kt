package dev.shorthouse.coinwatch.ui.screen.details.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.shorthouse.coinwatch.model.Price
import dev.shorthouse.coinwatch.ui.theme.AppTheme
import dev.shorthouse.coinwatch.ui.theme.NegativeRed
import dev.shorthouse.coinwatch.ui.theme.PositiveGreen
import java.math.BigDecimal

@Composable
fun ChartRangeLine(
    currentPrice: Price,
    minPrice: Price,
    maxPrice: Price,
    modifier: Modifier = Modifier
) {
    val latestMinPrice = remember(currentPrice, minPrice) { minOf(currentPrice, minPrice) }
    val latestMaxPrice = remember(currentPrice, maxPrice) { maxOf(currentPrice, maxPrice) }

    val currentMinDiff = remember(currentPrice, latestMinPrice) {
        currentPrice.amount - latestMinPrice.amount
    }

    val maxMinDiff = remember(latestMaxPrice, latestMinPrice) {
        latestMaxPrice.amount - latestMinPrice.amount
    }

    val currentPriceRatio = remember(currentMinDiff, maxMinDiff) {
        when {
            (currentMinDiff.compareTo(maxMinDiff) == 0) -> 0.5f
            (currentMinDiff.compareTo(BigDecimal.ZERO) == 0) -> 0f
            (maxMinDiff.compareTo(BigDecimal.ZERO) == 0) -> 1f
            else -> (currentMinDiff / maxMinDiff).toFloat()
        }
    }

    val positiveGreen = remember {
        PositiveGreen
    }

    val negativeRed = remember {
        NegativeRed
    }

    Canvas(modifier = modifier) {
        val canvasWidth = size.width
        val canvasHeightMiddle = size.height / 2f
        val lineWidth = 20f

        drawLine(
            start = Offset(
                x = canvasWidth * currentPriceRatio,
                y = canvasHeightMiddle
            ),
            end = Offset(
                x = canvasWidth,
                y = canvasHeightMiddle
            ),
            color = negativeRed,
            strokeWidth = lineWidth,
            cap = StrokeCap.Round
        )

        drawLine(
            start = Offset(
                x = 0f,
                y = canvasHeightMiddle
            ),
            end = Offset(
                x = canvasWidth * currentPriceRatio,
                y = canvasHeightMiddle
            ),
            color = positiveGreen,
            strokeWidth = lineWidth,
            cap = StrokeCap.Round
        )
    }
}

@Composable
@Preview
private fun PriceMinMaxLinePreview() {
    AppTheme {
        ChartRangeLine(
            currentPrice = Price("80.0"),
            minPrice = Price("70.0"),
            maxPrice = Price("100.0"),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        )
    }
}
