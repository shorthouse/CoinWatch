package dev.shorthouse.coinwatch.ui.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.PriceEntry
import dev.shorthouse.coinwatch.ui.model.ScrubData
import dev.shorthouse.coinwatch.ui.theme.AppTheme
import dev.shorthouse.coinwatch.ui.theme.NegativeRed
import dev.shorthouse.coinwatch.ui.theme.PositiveGreen
import dev.shorthouse.coinwatch.ui.theme.ZeroWhite
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.roundToInt

private const val GRAPH_ANIMATION_DURATION_MS = 800
private const val GRAPH_FILL_ALPHA = 0.5f
private const val SCRUB_FADED_FILL_ALPHA = 0.15f
private const val SCRUB_FADED_LINE_ALPHA = 0.3f
private const val SCRUB_GLOW_ALPHA = 0.4f
private val GRAPH_LINE_STROKE_WIDTH = 3.dp

@Composable
fun ScrubPriceGraph(
    priceHistory: ImmutableList<PriceEntry>,
    priceChangePercentage: Percentage,
    isGraphAnimated: Boolean,
    onScrub: (ScrubData?) -> Unit,
    modifier: Modifier = Modifier,
) {
    val prices = remember(priceHistory) { priceHistory.map { it.price }.toImmutableList() }

    val baseLineColor = remember(priceChangePercentage) { priceGraphColor(priceChangePercentage) }

    val progress = remember(priceHistory, isGraphAnimated) {
        Animatable(if (isGraphAnimated) 0f else 1f)
    }

    // Replay the reveal animation when the data series or animation flag changes.
    LaunchedEffect(priceHistory, isGraphAnimated) {
        if (isGraphAnimated) {
            progress.snapTo(0f)
            progress.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = GRAPH_ANIMATION_DURATION_MS,
                    easing = LinearEasing,
                ),
            )
        } else {
            progress.snapTo(1f)
        }
    }

    var selectedScrubIndex by remember { mutableStateOf<Int?>(null) }
    var graphPoints by remember { mutableStateOf<List<Offset>>(emptyList()) }

    val haptic = LocalHapticFeedback.current
    val currentOnScrub by rememberUpdatedState(onScrub)

    // Reset scrub state when the price series changes, such as a chart period switch mid-scrub.
    LaunchedEffect(priceHistory) {
        selectedScrubIndex = clearScrubState(
            selectedScrubIndex = selectedScrubIndex,
            onScrub = currentOnScrub,
        )
    }

    val selectedScrubData = remember(priceHistory, selectedScrubIndex) {
        selectedScrubIndex
            ?.takeIf { it in priceHistory.indices }
            ?.let { buildScrubData(priceHistory, it) }
    }

    val lineColor = selectedScrubData?.changePercentage?.let { pct ->
        priceGraphColor(pct)
    } ?: baseLineColor

    val fillColor = lineColor.copy(alpha = GRAPH_FILL_ALPHA)

    val scrubDateText = selectedScrubIndex
        ?.takeIf { it in priceHistory.indices }
        ?.let { priceHistory[it].formattedDate.ifEmpty { null } }

    Column(modifier = modifier) {
        // Fixed height keeps the graph from shifting when the date label appears.
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
                .testTag("scrubPriceGraphDateLabelContainer")
        ) {
            val selectedPoint = selectedScrubIndex
                ?.takeIf { it in graphPoints.indices }
                ?.let { graphPoints[it] }

            if (scrubDateText != null && selectedPoint != null) {
                val snappedXPx = selectedPoint.x

                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = MaterialTheme.shapes.extraSmall,
                    modifier = Modifier
                        .layout { measurable, constraints ->
                            val placeable = measurable.measure(constraints)
                            layout(constraints.maxWidth, placeable.height) {
                                val maxX = (constraints.maxWidth - placeable.width).coerceAtLeast(0)
                                val x = (snappedXPx - placeable.width / 2f)
                                    .toInt()
                                    .coerceIn(0, maxX)
                                placeable.placeRelative(x, 0)
                            }
                        }
                        .testTag("scrubPriceGraphDateLabel")
                ) {
                    Text(
                        text = scrubDateText,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }

        PriceGraphCanvas(
            prices = prices,
            canvasTag = "scrubPriceGraphCanvas",
            onPointsChanged = { graphPoints = it },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .pointerInput(priceHistory) {
                    if (priceHistory.size < 2) return@pointerInput

                    awaitEachGesture {
                        val down = awaitFirstDown()
                        if (progress.value < 1f) return@awaitEachGesture

                        if (size.width <= 0) return@awaitEachGesture

                        var isScrubbing = false
                        try {
                            val firstScrubUpdate = updateScrubState(
                                priceHistorySnapshot = priceHistory,
                                xPosition = down.position.x,
                                canvasWidth = size.width.toFloat(),
                                selectedScrubIndex = selectedScrubIndex,
                            )
                            if (firstScrubUpdate == null) return@awaitEachGesture

                            selectedScrubIndex = firstScrubUpdate.selectedIndex
                            firstScrubUpdate.scrubData?.let { scrubData ->
                                currentOnScrub(scrubData)
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            }
                            isScrubbing = true

                            down.consume()

                            val activePointerId = down.id
                            while (true) {
                                val event = awaitPointerEvent()
                                val change = event.changes
                                    .firstOrNull { it.id == activePointerId }
                                    ?: break

                                if (!change.pressed) {
                                    change.consume()
                                    break
                                }

                                val scrubUpdate = updateScrubState(
                                    priceHistorySnapshot = priceHistory,
                                    xPosition = change.position.x,
                                    canvasWidth = size.width.toFloat(),
                                    selectedScrubIndex = selectedScrubIndex,
                                )
                                if (scrubUpdate != null) {
                                    selectedScrubIndex = scrubUpdate.selectedIndex
                                    scrubUpdate.scrubData?.let { scrubData ->
                                        currentOnScrub(scrubData)
                                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    }
                                }
                                change.consume()
                            }
                        } finally {
                            if (isScrubbing) {
                                selectedScrubIndex = clearScrubState(
                                    selectedScrubIndex = selectedScrubIndex,
                                    onScrub = currentOnScrub,
                                )
                            }
                        }
                    }
                }
        ) { paths, points ->
            val scrubIndex = selectedScrubIndex
            if (scrubIndex != null && scrubIndex in points.indices && progress.value >= 1f) {
                drawScrubbedPriceGraph(
                    paths = paths,
                    lineColor = lineColor,
                    fillColor = fillColor,
                    scrubPoint = points[scrubIndex],
                )
            } else {
                drawAnimatedPriceGraph(
                    paths = paths,
                    lineColor = lineColor,
                    fillColor = fillColor,
                    progress = progress.value,
                )
            }
        }
    }
}

@Composable
fun StaticPriceGraph(
    prices: ImmutableList<BigDecimal>,
    priceChangePercentage: Percentage,
    modifier: Modifier = Modifier,
) {
    val lineColor = remember(priceChangePercentage) { priceGraphColor(priceChangePercentage) }

    val fillColor = lineColor.copy(alpha = GRAPH_FILL_ALPHA)

    Box(modifier = modifier) {
        PriceGraphCanvas(
            prices = prices,
            canvasTag = "staticPriceGraphCanvas",
            modifier = Modifier.matchParentSize(),
        ) { paths, _ ->
            drawPriceGraph(
                paths = paths,
                lineColor = lineColor,
                fillColor = fillColor,
            )
        }
    }
}

private data class PriceGraphPaths(
    val line: Path,
    val fill: Path,
)

private data class ScrubStateUpdate(
    val selectedIndex: Int,
    val scrubData: ScrubData?,
)

@Composable
private fun PriceGraphCanvas(
    prices: ImmutableList<BigDecimal>,
    canvasTag: String,
    modifier: Modifier = Modifier,
    onPointsChanged: (List<Offset>) -> Unit = {},
    drawGraph: DrawScope.(PriceGraphPaths, List<Offset>) -> Unit,
) {
    val minPrice = remember(prices) { prices.minOrNull() }
    val maxPrice = remember(prices) { prices.maxOrNull() }
    val graphVerticalInset = with(LocalDensity.current) { GRAPH_LINE_STROKE_WIDTH.toPx() }

    var canvasSize by remember { mutableStateOf(Size.Zero) }

    val points = remember(prices, canvasSize, graphVerticalInset) {
        if (canvasSize == Size.Zero || minPrice == null || maxPrice == null) {
            emptyList()
        } else {
            mapPricesToPoints(
                prices = prices,
                minPrice = minPrice,
                maxPrice = maxPrice,
                canvasWidth = canvasSize.width,
                canvasHeight = canvasSize.height,
                verticalInset = graphVerticalInset,
            )
        }
    }

    val currentOnPointsChanged by rememberUpdatedState(onPointsChanged)
    LaunchedEffect(points) {
        currentOnPointsChanged(points)
    }

    Canvas(
        modifier = modifier
            .onSizeChanged { canvasSize = it.toSize() }
            .testTag(canvasTag)
    ) {
        if (minPrice == null || maxPrice == null) return@Canvas

        val drawPoints = mapPricesToPoints(
            prices = prices,
            minPrice = minPrice,
            maxPrice = maxPrice,
            canvasWidth = size.width,
            canvasHeight = size.height,
            verticalInset = graphVerticalInset,
        )
        val paths = buildPriceGraphPaths(points = drawPoints, canvasHeight = size.height)
            ?: return@Canvas

        drawGraph(paths, drawPoints)
    }
}

private fun priceGraphColor(percentage: Percentage): Color =
    when {
        percentage.isPositive -> PositiveGreen
        percentage.isNegative -> NegativeRed
        else -> ZeroWhite
    }

private fun calculateScrubIndex(
    xPosition: Float,
    canvasWidth: Float,
    lastIndex: Int,
): Int {
    if (canvasWidth <= 0f || lastIndex < 0) return -1

    return (xPosition.coerceIn(0f, canvasWidth) / canvasWidth * lastIndex)
        .roundToInt()
        .coerceIn(0, lastIndex)
}

private fun clearScrubState(
    selectedScrubIndex: Int?,
    onScrub: (ScrubData?) -> Unit,
): Int? {
    if (selectedScrubIndex != null) {
        onScrub(null)
    }

    return null
}

private fun updateScrubState(
    priceHistorySnapshot: ImmutableList<PriceEntry>,
    xPosition: Float,
    canvasWidth: Float,
    selectedScrubIndex: Int?,
): ScrubStateUpdate? {
    val index = calculateScrubIndex(
        xPosition = xPosition,
        canvasWidth = canvasWidth,
        lastIndex = priceHistorySnapshot.lastIndex,
    )
    if (index == -1) return null

    return ScrubStateUpdate(
        selectedIndex = index,
        scrubData = if (index != selectedScrubIndex) {
            buildScrubData(priceHistorySnapshot, index)
        } else {
            null
        },
    )
}

private fun buildScrubData(
    priceHistory: ImmutableList<PriceEntry>,
    index: Int,
): ScrubData {
    val scrubbedPrice = priceHistory[index].price
    val firstPrice = priceHistory.first().price
    val changeAmount = scrubbedPrice - firstPrice
    val changePercentage = if (firstPrice.compareTo(BigDecimal.ZERO) != 0) {
        changeAmount.divide(firstPrice, 4, RoundingMode.HALF_UP)
            .multiply(BigDecimal(100))
    } else {
        BigDecimal.ZERO
    }
    return ScrubData(
        price = scrubbedPrice,
        timestamp = priceHistory[index].timestamp,
        changePercentage = Percentage(changePercentage.toPlainString()),
        changeAmount = changeAmount,
    )
}

private fun buildPriceGraphPaths(
    points: List<Offset>,
    canvasHeight: Float,
): PriceGraphPaths? {
    if (points.size < 2) return null

    return PriceGraphPaths(
        line = buildSmoothedPath(points),
        fill = buildSmoothedPath(points, closedToBottom = canvasHeight),
    )
}

private fun DrawScope.drawAnimatedPriceGraph(
    paths: PriceGraphPaths,
    lineColor: Color,
    fillColor: Color,
    progress: Float,
) {
    clipRect(right = size.width * progress) {
        drawPriceGraph(
            paths = paths,
            lineColor = lineColor,
            fillColor = fillColor,
        )
    }
}

private fun DrawScope.drawScrubbedPriceGraph(
    paths: PriceGraphPaths,
    lineColor: Color,
    fillColor: Color,
    scrubPoint: Offset,
) {
    clipRect(right = scrubPoint.x) {
        drawPriceGraph(
            paths = paths,
            lineColor = lineColor,
            fillColor = fillColor,
        )
    }

    clipRect(left = scrubPoint.x) {
        drawPriceGraph(
            paths = paths,
            lineColor = lineColor.copy(alpha = SCRUB_FADED_LINE_ALPHA),
            fillColor = fillColor.copy(alpha = SCRUB_FADED_FILL_ALPHA),
        )
    }

    drawLine(
        color = lineColor,
        start = Offset(scrubPoint.x, 0f),
        end = Offset(scrubPoint.x, size.height),
        strokeWidth = 2.dp.toPx(),
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(8.dp.toPx(), 6.dp.toPx())),
    )

    drawCircle(
        color = lineColor.copy(alpha = SCRUB_GLOW_ALPHA),
        radius = 10.dp.toPx(),
        center = scrubPoint,
    )

    drawCircle(
        color = lineColor,
        radius = 4.dp.toPx(),
        center = scrubPoint,
    )
}

private fun DrawScope.drawPriceGraph(
    paths: PriceGraphPaths,
    lineColor: Color,
    fillColor: Color,
) {
    drawPath(
        path = paths.fill,
        brush = Brush.verticalGradient(
            colors = listOf(fillColor, Color.Transparent),
            endY = size.height,
        ),
    )
    drawPath(
        path = paths.line,
        color = lineColor,
        style = Stroke(width = GRAPH_LINE_STROKE_WIDTH.toPx(), cap = StrokeCap.Round),
    )
}

// Double arithmetic avoids ArithmeticException for non-terminating decimal ratios.
private fun mapPricesToPoints(
    prices: List<BigDecimal>,
    minPrice: BigDecimal,
    maxPrice: BigDecimal,
    canvasWidth: Float,
    canvasHeight: Float,
    verticalInset: Float,
): List<Offset> {
    val min = minPrice.toDouble()
    val max = maxPrice.toDouble()
    val range = max - min
    val lastIndex = prices.lastIndex.coerceAtLeast(1).toFloat()
    val boundedInset = verticalInset.coerceAtMost(canvasHeight / 2f)
    val drawableHeight = canvasHeight - (boundedInset * 2f)

    return prices.mapIndexed { index, price ->
        val x = index / lastIndex * canvasWidth
        val y = if (range == 0.0) {
            canvasHeight / 2f
        } else {
            val ratio = ((price.toDouble() - min) / range).toFloat()
            boundedInset + drawableHeight * (1f - ratio)
        }
        Offset(x, y)
    }
}

/**
 * Builds a smooth cubic-bezier path through [points].
 *
 * When [closedToBottom] is non-null, the path is extended down to that
 * y-coordinate on both sides and closed, producing a fill shape.
 */
private fun buildSmoothedPath(
    points: List<Offset>,
    closedToBottom: Float? = null,
): Path = Path().apply {
    if (closedToBottom != null) {
        moveTo(points.first().x, closedToBottom)
        lineTo(points.first().x, points.first().y)
    } else {
        moveTo(points.first().x, points.first().y)
    }

    for (i in 1 until points.size) {
        val prev = points[i - 1]
        val curr = points[i]
        val midX = (prev.x + curr.x) / 2f
        cubicTo(midX, prev.y, midX, curr.y, curr.x, curr.y)
    }

    if (closedToBottom != null) {
        lineTo(points.last().x, closedToBottom)
        close()
    }
}

@Composable
@Preview(showBackground = true)
private fun ScrubPriceGraphPreview() {
    AppTheme {
        ScrubPriceGraph(
            onScrub = {},
            priceHistory = persistentListOf(
                PriceEntry(BigDecimal("1650.19"), 1700000000L, "14 Nov 2023"),
                PriceEntry(BigDecimal("1650.71"), 1700003600L, "14 Nov 2023"),
                PriceEntry(BigDecimal("1670.94"), 1700007200L, "15 Nov 2023"),
                PriceEntry(BigDecimal("1680.44"), 1700010800L, "15 Nov 2023"),
                PriceEntry(BigDecimal("1743.98"), 1700014400L, "15 Nov 2023"),
                PriceEntry(BigDecimal("1740.25"), 1700018000L, "15 Nov 2023"),
                PriceEntry(BigDecimal("1737.53"), 1700021600L, "15 Nov 2023"),
                PriceEntry(BigDecimal("1730.56"), 1700025200L, "15 Nov 2023"),
                PriceEntry(BigDecimal("1738.12"), 1700028800L, "15 Nov 2023"),
                PriceEntry(BigDecimal("1736.10"), 1700032400L, "15 Nov 2023"),
            ),
            priceChangePercentage = Percentage("0.42"),
            isGraphAnimated = false,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun StaticPriceGraphPreview() {
    AppTheme {
        StaticPriceGraph(
            prices = persistentListOf(
                BigDecimal("1650.19"),
                BigDecimal("1650.71"),
                BigDecimal("1670.94"),
                BigDecimal("1680.44"),
                BigDecimal("1743.98"),
                BigDecimal("1740.25"),
                BigDecimal("1737.53"),
                BigDecimal("1730.56"),
            ),
            priceChangePercentage = Percentage("0.42"),
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
        )
    }
}
