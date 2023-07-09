package dev.shorthouse.cryptodata.ui.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import dev.shorthouse.cryptodata.model.Percentage
import dev.shorthouse.cryptodata.ui.theme.AppTheme
import dev.shorthouse.cryptodata.ui.theme.NegativeRed
import dev.shorthouse.cryptodata.ui.theme.PositiveGreen
import dev.shorthouse.cryptodata.ui.theme.ZeroWhite
import kotlin.math.ceil

@Composable
fun PriceGraph(
    prices: List<Double>,
    priceChangePercentage: Percentage,
    modifier: Modifier = Modifier
) {
    val minPrice = remember(prices) {
        prices.minOrNull() ?: 0.0
    }

    val maxPrice = remember(prices) {
        ceil(prices.maxOrNull() ?: 0.0)
    }

    val graphLineColor = remember(priceChangePercentage) {
        when {
            priceChangePercentage.isPositive -> PositiveGreen
            priceChangePercentage.isNegative -> NegativeRed
            else -> ZeroWhite
        }
    }

    val graphFillGradientColor = remember(graphLineColor) {
        graphLineColor.copy(alpha = 0.5f)
    }

    Canvas(modifier = modifier.animateContentSize()) {
        val spacePerPoint = size.width / prices.size
        var lastX = 0f

        val strokePath = Path().apply {
            val height = size.height

            for ((index, price) in prices.withIndex()) {
                val leftRatio = (price - minPrice) / (maxPrice - minPrice)
                val currentX = index * spacePerPoint
                val currentY = height - (leftRatio * height).toFloat()

                val nextPrice = prices.getOrNull(index + 1) ?: prices.last()
                val rightRatio = (nextPrice - minPrice) / (maxPrice - minPrice)
                val nextX = (index + 1) * spacePerPoint
                val nextY = height - (rightRatio * height).toFloat()

                lastX = (currentX + nextX) / 2f

                if (index == 0) {
                    moveTo(currentX, currentY)
                }

                quadraticBezierTo(
                    currentX,
                    currentY,
                    lastX,
                    (currentY + nextY) / 2f
                )
            }
        }

        drawPath(
            path = strokePath,
            color = graphLineColor,
            style = Stroke(
                width = 3.dp.toPx(),
                cap = StrokeCap.Round
            )
        )

        val fillPath = android.graphics.Path(strokePath.asAndroidPath())
            .asComposePath()
            .apply {
                lineTo(lastX, size.height)
                lineTo(0f, size.height)
                close()
            }

        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(
                    graphFillGradientColor,
                    Color.Transparent
                ),
                endY = size.height
            )
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun PriceChartPreview() {
    AppTheme {
        PriceGraph(
            prices = listOf(
                1650.19, 1650.71, 1670.94, 1680.44, 1743.98, 1740.25, 1737.53, 1730.56, 1738.12, 1736.10, 1740.20, 1740.64, 1741.49, 1738.87, 1734.92, 1736.79, 1743.53, 1743.21, 1744.75, 1744.85, 1741.76, 1741.46, 1739.82, 1740.15, 1745.08, 1743.29, 1746.12, 1745.99, 1744.89, 1741.10, 1741.91, 1738.47, 1737.67, 1741.82, 1735.95, 1728.11, 1657.23, 1649.89, 1649.71, 1650.68, 1654.04, 1648.55, 1650.10, 1651.87, 1651.29, 1642.75, 1637.79, 1635.80, 1637.01, 1632.46, 1633.31, 1640.08, 1638.61, 1645.47, 1643.50, 1640.57, 1640.41, 1641.38, 1660.21, 1665.73, 1660.33, 1665.65, 1664.11, 1665.71, 1661.90, 1661.17, 1662.54, 1665.58, 1666.27, 1669.82, 1671.34, 1669.87, 1670.62, 1668.97, 1668.86, 1664.58, 1665.96, 1664.53, 1656.15, 1670.91, 1685.59, 1693.69, 1718.10, 1719.56, 1724.42, 1717.22, 1718.34, 1716.38, 1715.37, 1716.46, 1719.39, 1717.94, 1722.92, 1755.97, 1749.11, 1742.58, 1742.88, 1743.36, 1742.95, 1739.68, 1736.65, 1739.88, 1734.35, 1727.31, 1728.35, 1724.05, 1730.04, 1726.87, 1727.71, 1728.49, 1729.93, 1726.37, 1722.92, 1726.67, 1724.76, 1728.41, 1729.20, 1728.20, 1727.98, 1729.96, 1727.80, 1732.04, 1730.22, 1733.16, 1734.14, 1734.31, 1739.62, 1737.76, 1739.52, 1742.98, 1738.36
            ),
            priceChangePercentage = Percentage(0.42),
            modifier = Modifier.fillMaxWidth().height(250.dp)
        )
    }
}
