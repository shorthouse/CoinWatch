package dev.shorthouse.cryptodata.ui.screen.detail.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import dev.shorthouse.cryptodata.model.Price
import dev.shorthouse.cryptodata.ui.theme.AppTheme
import dev.shorthouse.cryptodata.ui.theme.NegativeRed
import dev.shorthouse.cryptodata.ui.theme.PositiveGreen
import java.math.BigDecimal

@Composable
fun ChartRangeLine(
    currentPrice: Price,
    minPrice: Price,
    maxPrice: Price,
    modifier: Modifier = Modifier
) {
    val currentMinDiff = currentPrice.amount - minPrice.amount
    val maxMinDiff = maxPrice.amount - minPrice.amount

    val currentPriceRatio = when {
        (currentMinDiff.compareTo(BigDecimal.ZERO) == 0) -> 0f
        (maxMinDiff.compareTo(BigDecimal.ZERO) == 0) -> 1f
        else -> currentMinDiff / maxMinDiff
    }

    val positiveGreen = remember {
        PositiveGreen
    }

    val negativeRed = remember {
        NegativeRed
    }

    val dividerColor = MaterialTheme.colorScheme.onSurface

    Canvas(modifier = modifier) {
        val canvasWidth = size.width
        val canvasHeightMiddle = size.height / 2f
        val lineWidth = 30f

        drawLine(
            start = Offset(
                x = 0f,
                y = canvasHeightMiddle
            ),
            end = Offset(
                x = canvasWidth * currentPriceRatio.toFloat(),
                y = canvasHeightMiddle
            ),
            color = positiveGreen,
            strokeWidth = lineWidth,
            cap = StrokeCap.Round
        )

        drawLine(
            start = Offset(
                x = canvasWidth * currentPriceRatio.toFloat(),
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
                x = canvasWidth * currentPriceRatio.toFloat(),
                y = canvasHeightMiddle - (lineWidth / 2f)
            ),
            end = Offset(
                x = canvasWidth * currentPriceRatio.toFloat(),
                y = canvasHeightMiddle + (lineWidth / 2f)
            ),
            color = dividerColor,
            strokeWidth = lineWidth,
            cap = StrokeCap.Round
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun PriceMinMaxLinePreview() {
    AppTheme {
        ChartRangeLine(
            currentPrice = Price(BigDecimal("100.0")),
            minPrice = Price(BigDecimal("80.0")),
            maxPrice = Price(BigDecimal("100.0")),
            modifier = Modifier.fillMaxSize()
        )
    }
}
