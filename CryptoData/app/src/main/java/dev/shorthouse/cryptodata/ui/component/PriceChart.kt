package dev.shorthouse.cryptodata.ui.component
import androidx.compose.animation.core.AnimationState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateTo
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.shorthouse.cryptodata.model.Percentage
import dev.shorthouse.cryptodata.ui.theme.AppTheme
import dev.shorthouse.cryptodata.ui.theme.NegativeRed
import dev.shorthouse.cryptodata.ui.theme.PositiveGreen
import dev.shorthouse.cryptodata.ui.theme.ZeroWhite
import java.math.BigDecimal
import java.math.RoundingMode

@Composable
fun PriceGraph(
    prices: List<BigDecimal>,
    priceChangePercentage: Percentage,
    modifier: Modifier = Modifier
) {
    val minPrice = remember(prices) {
        prices.minOrNull() ?: BigDecimal.ZERO
    }

    val maxPrice = remember(prices) {
        (prices.maxOrNull() ?: BigDecimal.ZERO)
            .setScale(0, RoundingMode.CEILING)
    }

    val graphLineColor = remember(priceChangePercentage) {
        when {
            priceChangePercentage.isPositive -> PositiveGreen
            priceChangePercentage.isNegative -> NegativeRed
            else -> ZeroWhite
        }
    }

    val graphFillGradientColor = remember(graphLineColor) {
        graphLineColor.copy(alpha = 0.6f)
    }

    val animatedPathProgress = remember(prices) {
        AnimationState(0f)
    }

    Canvas(modifier = modifier) {
        val spacePerPoint = size.width / prices.size
        val height = size.height

        val strokePath = Path().apply {
            for ((index, price) in prices.withIndex()) {
                val currentX = index * spacePerPoint
                val currentRatio = (price - minPrice) / (maxPrice - minPrice)
                val currentY = height - (currentRatio * height.toBigDecimal()).toFloat()

                val nextX = (index + 1) * spacePerPoint
                val nextPrice = prices.getOrNull(index + 1) ?: prices.last()
                val nextRatio = (nextPrice - minPrice) / (maxPrice - minPrice)
                val nextY = height - (nextRatio * height.toBigDecimal()).toFloat()

                if (index == 0) {
                    moveTo(currentX, currentY)
                }

                quadraticBezierTo(
                    currentX,
                    currentY,
                    (currentX + nextX) / 2f,
                    (currentY + nextY) / 2f
                )
            }
        }

        val animatedLinePath = derivedStateOf {
            val pathMeasure = PathMeasure()
            pathMeasure.setPath(strokePath, false)
            val animatedLinePath = Path()

            pathMeasure.getSegment(
                0f,
                animatedPathProgress.value * pathMeasure.length,
                animatedLinePath
            )

            animatedLinePath
        }

        drawLinePath(
            linePath = animatedLinePath.value,
            lineColor = graphLineColor
        )

        drawFillPath(
            fillPath = strokePath.apply {
                lineTo(2000f, size.height)
                lineTo(0f, size.height)
            },
            fillColor = graphFillGradientColor
        )
    }

    LaunchedEffect(prices) {
        animatedPathProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 1000,
                easing = LinearEasing
            )
        )
    }
}

private fun DrawScope.drawLinePath(
    linePath: Path,
    lineColor: Color
) {
    drawPath(
        path = linePath,
        color = lineColor,
        style = Stroke(
            width = 3.dp.toPx(),
            cap = StrokeCap.Round
        )
    )
}

private fun DrawScope.drawFillPath(
    fillPath: Path,
    fillColor: Color
) {
    drawPath(
        path = fillPath,
        brush = Brush.verticalGradient(
            colors = listOf(
                fillColor,
                Color.Transparent
            ),
            endY = size.height
        )
    )
}

@Composable
@Preview(showBackground = true)
private fun PriceChartPreview() {
    AppTheme {
        PriceGraph(
            prices = listOf(
                BigDecimal("1650.19"), BigDecimal("1650.71"), BigDecimal("1670.94"), BigDecimal("1680.44"), BigDecimal("1743.98"), BigDecimal("1740.25"), BigDecimal("1737.53"), BigDecimal("1730.56"), BigDecimal("1738.12"), BigDecimal("1736.10"), BigDecimal("1740.20"), BigDecimal("1740.64"), BigDecimal("1741.49"), BigDecimal("1738.87"), BigDecimal("1734.92"), BigDecimal("1736.79"), BigDecimal("1743.53"), BigDecimal("1743.21"), BigDecimal("1744.75"), BigDecimal("1744.85"), BigDecimal("1741.76"), BigDecimal("1741.46"), BigDecimal("1739.82"), BigDecimal("1740.15"), BigDecimal("1745.08"), BigDecimal("1743.29"), BigDecimal("1746.12"), BigDecimal("1745.99"), BigDecimal("1744.89"), BigDecimal("1741.10"), BigDecimal("1741.91"), BigDecimal("1738.47"), BigDecimal("1737.67"), BigDecimal("1741.82"), BigDecimal("1735.95"), BigDecimal("1728.11"), BigDecimal("1657.23"), BigDecimal("1649.89"), BigDecimal("1649.71"), BigDecimal("1650.68"), BigDecimal("1654.04"), BigDecimal("1648.55"), BigDecimal("1650.10"), BigDecimal("1651.87"), BigDecimal("1651.29"), BigDecimal("1642.75"), BigDecimal("1637.79"), BigDecimal("1635.80"), BigDecimal("1637.01"), BigDecimal("1632.46"), BigDecimal("1633.31"), BigDecimal("1640.08"), BigDecimal("1638.61"), BigDecimal("1645.47"), BigDecimal("1643.50"), BigDecimal("1640.57"), BigDecimal("1640.41"), BigDecimal("1641.38"), BigDecimal("1660.21"), BigDecimal("1665.73"), BigDecimal("1660.33"), BigDecimal("1665.65"), BigDecimal("1664.11"), BigDecimal("1665.71"), BigDecimal("1661.90"), BigDecimal("1661.17"), BigDecimal("1662.54"), BigDecimal("1665.58"), BigDecimal("1666.27"), BigDecimal("1669.82"), BigDecimal("1671.34"), BigDecimal("1669.87"), BigDecimal("1670.62"), BigDecimal("1668.97"), BigDecimal("1668.86"), BigDecimal("1664.58"), BigDecimal("1665.96"), BigDecimal("1664.53"), BigDecimal("1656.15"), BigDecimal("1670.91"), BigDecimal("1685.59"), BigDecimal("1693.69"), BigDecimal("1718.10"), BigDecimal("1719.56"), BigDecimal("1724.42"), BigDecimal("1717.22"), BigDecimal("1718.34"), BigDecimal("1716.38"), BigDecimal("1715.37"), BigDecimal("1716.46"), BigDecimal("1719.39"), BigDecimal("1717.94"), BigDecimal("1722.92"), BigDecimal("1755.97"), BigDecimal("1749.11"), BigDecimal("1742.58"), BigDecimal("1742.88"), BigDecimal("1743.36"), BigDecimal("1742.95"), BigDecimal("1739.68"), BigDecimal("1736.65"), BigDecimal("1739.88"), BigDecimal("1734.35"), BigDecimal("1727.31"), BigDecimal("1728.35"), BigDecimal("1724.05"), BigDecimal("1730.04"), BigDecimal("1726.87"), BigDecimal("1727.71"), BigDecimal("1728.49"), BigDecimal("1729.93"), BigDecimal("1726.37"), BigDecimal("1722.92"), BigDecimal("1726.67"), BigDecimal("1724.76"), BigDecimal("1728.41"), BigDecimal("1729.20"), BigDecimal("1728.20"), BigDecimal("1727.98"), BigDecimal("1729.96"), BigDecimal("1727.80"), BigDecimal("1732.04"), BigDecimal("1730.22"), BigDecimal("1733.16"), BigDecimal("1734.14"), BigDecimal("1734.31"), BigDecimal("1739.62"), BigDecimal("1737.76"), BigDecimal("1739.52"), BigDecimal("1742.98"), BigDecimal("1738.36") // ktlint-disable argument-list-wrapping
            ),
            priceChangePercentage = Percentage(BigDecimal("0.42")),
            modifier = Modifier.fillMaxWidth().height(250.dp)
        )
    }
}
