package dev.shorthouse.coinwatch.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.unit.dp
import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.PriceEntry
import dev.shorthouse.coinwatch.ui.model.ScrubData
import dev.shorthouse.coinwatch.ui.theme.AppTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.junit.Rule
import org.junit.Test
import java.math.BigDecimal

class PriceGraphTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun when_staticPriceGraphHasData_should_drawVisiblePixels() {
        composeTestRule.setContent {
            AppTheme {
                StaticPriceGraph(
                    prices = sampleStaticPrices,
                    priceChangePercentage = Percentage("5.12"),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                )
            }
        }

        composeTestRule.onNodeWithTag(STATIC_CANVAS_TAG, useUnmergedTree = true)
            .assertIsDisplayed()

        assertNodeHasDrawnPixels(STATIC_CANVAS_TAG)
    }

    @Test
    fun when_staticPriceGraphUsesFlatPrices_should_stillDrawGraph() {
        composeTestRule.setContent {
            AppTheme {
                StaticPriceGraph(
                    prices = persistentListOf(
                        BigDecimal("10.00"),
                        BigDecimal("10.00"),
                        BigDecimal("10.00"),
                        BigDecimal("10.00")
                    ),
                    priceChangePercentage = Percentage("0"),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                )
            }
        }

        composeTestRule.onNodeWithTag(STATIC_CANVAS_TAG, useUnmergedTree = true)
            .assertIsDisplayed()

        assertNodeHasDrawnPixels(STATIC_CANVAS_TAG)
    }

    @Test
    fun when_staticPriceGraphMaxPriceTouchesTop_should_leaveTopEdgeClear() {
        composeTestRule.setContent {
            AppTheme {
                StaticPriceGraph(
                    prices = persistentListOf(
                        BigDecimal("100.00"),
                        BigDecimal("90.00"),
                        BigDecimal("80.00"),
                        BigDecimal("70.00")
                    ),
                    priceChangePercentage = Percentage("-30.00"),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                )
            }
        }

        composeTestRule.onNodeWithTag(STATIC_CANVAS_TAG, useUnmergedTree = true)
            .assertIsDisplayed()

        assertNodeTopRowIsUniform(STATIC_CANVAS_TAG)
        assertNodeHasDrawnPixels(STATIC_CANVAS_TAG)
    }

    @Test
    fun when_staticPriceGraphHasSinglePrice_should_renderWithoutDrawingFailure() {
        composeTestRule.setContent {
            AppTheme {
                StaticPriceGraph(
                    prices = persistentListOf(BigDecimal("10.00")),
                    priceChangePercentage = Percentage("0"),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                )
            }
        }

        composeTestRule.onNodeWithTag(STATIC_CANVAS_TAG, useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun when_staticPriceGraphHasCallerTestTag_should_preserveCallerTagAndInternalCanvasTag() {
        composeTestRule.setContent {
            AppTheme {
                StaticPriceGraph(
                    prices = sampleStaticPrices,
                    priceChangePercentage = Percentage("5.12"),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .testTag(CALLER_STATIC_GRAPH_TAG)
                )
            }
        }

        composeTestRule.onNodeWithTag(CALLER_STATIC_GRAPH_TAG, useUnmergedTree = true)
            .assertIsDisplayed()
        composeTestRule.onNodeWithTag(STATIC_CANVAS_TAG, useUnmergedTree = true)
            .assertIsDisplayed()
        assertNodeHasDrawnPixels(STATIC_CANVAS_TAG)
    }

    @Test
    fun when_scrubPriceGraphIsTouched_should_emitScrubDataShowDateAndClearOnRelease() {
        val scrubEvents = mutableListOf<ScrubData?>()

        composeTestRule.setContent {
            AppTheme {
                ScrubPriceGraph(
                    priceHistory = samplePriceHistory,
                    priceChangePercentage = Percentage("12.50"),
                    isGraphAnimated = false,
                    onScrub = { scrubEvents += it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                )
            }
        }

        composeTestRule.onAllNodesWithTag(SCRUB_DATE_LABEL_TAG, useUnmergedTree = true)
            .assertCountEquals(0)

        composeTestRule.onNodeWithTag(SCRUB_CANVAS_TAG, useUnmergedTree = true)
            .performTouchInput {
                down(Offset(x = width / 2f, y = height / 2f))
            }

        composeTestRule.waitForIdle()

        assertThat(scrubEvents).isNotEmpty()
        val scrubData = scrubEvents.first()
        assertThat(scrubData).isNotNull()
        assertThat(scrubData?.timestamp).isEqualTo(samplePriceHistory[2].timestamp)
        assertThat(scrubData?.price).isEqualTo(samplePriceHistory[2].price)
        assertThat(scrubData?.changeAmount).isEqualTo(BigDecimal("20.00"))
        assertThat(scrubData?.changePercentage?.formattedAmount).isEqualTo("+20.00%")

        composeTestRule.onNodeWithTag(SCRUB_DATE_LABEL_TAG, useUnmergedTree = true)
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("03 Jan").assertIsDisplayed()

        composeTestRule.onNodeWithTag(SCRUB_CANVAS_TAG, useUnmergedTree = true)
            .performTouchInput { up() }

        composeTestRule.waitForIdle()

        assertThat(scrubEvents.last()).isNull()
        composeTestRule.onAllNodesWithTag(SCRUB_DATE_LABEL_TAG, useUnmergedTree = true)
            .assertCountEquals(0)
    }

    @Test
    fun when_scrubMovesAcrossGraph_shouldUpdateToNearestPoint() {
        var latestScrubData: ScrubData? = null

        composeTestRule.setContent {
            AppTheme {
                ScrubPriceGraph(
                    priceHistory = samplePriceHistory,
                    priceChangePercentage = Percentage("12.50"),
                    isGraphAnimated = false,
                    onScrub = { latestScrubData = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                )
            }
        }

        composeTestRule.onNodeWithTag(SCRUB_CANVAS_TAG, useUnmergedTree = true)
            .performTouchInput {
                down(Offset(x = 1f, y = height / 2f))
                moveTo(Offset(x = width - 1f, y = height / 2f))
            }

        composeTestRule.waitForIdle()

        assertThat(latestScrubData).isNotNull()
        assertThat(latestScrubData?.timestamp).isEqualTo(samplePriceHistory.last().timestamp)
        assertThat(latestScrubData?.changeAmount).isEqualTo(BigDecimal("40.00"))
        assertThat(latestScrubData?.changePercentage?.formattedAmount).isEqualTo("+40.00%")
        composeTestRule.onNodeWithText("05 Jan").assertIsDisplayed()
    }

    @Test
    fun when_scrubPriceGraphIsAnimating_should_ignoreInputUntilAnimationCompletes() {
        val scrubEvents = mutableListOf<ScrubData?>()
        composeTestRule.mainClock.autoAdvance = false

        composeTestRule.setContent {
            AppTheme {
                ScrubPriceGraph(
                    priceHistory = samplePriceHistory,
                    priceChangePercentage = Percentage("12.50"),
                    isGraphAnimated = true,
                    onScrub = { scrubEvents += it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                )
            }
        }

        composeTestRule.onNodeWithTag(SCRUB_CANVAS_TAG, useUnmergedTree = true)
            .performTouchInput {
                down(Offset(x = width / 2f, y = height / 2f))
                up()
            }

        composeTestRule.waitForIdle()
        assertThat(scrubEvents).isEmpty()

        composeTestRule.mainClock.advanceTimeBy(900)
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag(SCRUB_CANVAS_TAG, useUnmergedTree = true)
            .performTouchInput {
                down(Offset(x = width / 2f, y = height / 2f))
            }

        composeTestRule.waitForIdle()

        assertThat(scrubEvents).isNotEmpty()
        assertThat(scrubEvents.first()?.timestamp).isEqualTo(samplePriceHistory[2].timestamp)
        composeTestRule.mainClock.autoAdvance = true
    }

    @Test
    fun when_priceHistoryChangesMidScrubToShorterHistory_should_resetScrubState() {
        val scrubEvents = mutableListOf<ScrubData?>()
        var priceHistory by mutableStateOf(samplePriceHistory)

        composeTestRule.setContent {
            AppTheme {
                ScrubPriceGraph(
                    priceHistory = priceHistory,
                    priceChangePercentage = Percentage("12.50"),
                    isGraphAnimated = false,
                    onScrub = { scrubEvents += it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                )
            }
        }

        composeTestRule.onNodeWithTag(SCRUB_CANVAS_TAG, useUnmergedTree = true)
            .performTouchInput {
                down(Offset(x = width / 2f, y = height / 2f))
            }

        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("03 Jan").assertIsDisplayed()

        composeTestRule.runOnIdle {
            priceHistory = shorterReplacementPriceHistory
        }

        composeTestRule.waitForIdle()

        assertThat(scrubEvents.last()).isNull()
        composeTestRule.onAllNodesWithTag(SCRUB_DATE_LABEL_TAG, useUnmergedTree = true)
            .assertCountEquals(0)
    }

    @Test
    fun when_scrubPriceGraphHasSinglePoint_should_notEmitScrubData() {
        val scrubEvents = mutableListOf<ScrubData?>()

        composeTestRule.setContent {
            AppTheme {
                ScrubPriceGraph(
                    priceHistory = persistentListOf(
                        PriceEntry(
                            price = BigDecimal("100.00"),
                            timestamp = 1L,
                            formattedDate = "01 Jan"
                        )
                    ),
                    priceChangePercentage = Percentage("0"),
                    isGraphAnimated = false,
                    onScrub = { scrubEvents += it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                )
            }
        }

        composeTestRule.onNodeWithTag(SCRUB_CANVAS_TAG, useUnmergedTree = true)
            .performTouchInput {
                down(Offset(x = width / 2f, y = height / 2f))
                up()
            }

        composeTestRule.waitForIdle()

        assertThat(scrubEvents).isEmpty()
        composeTestRule.onAllNodesWithTag(SCRUB_DATE_LABEL_TAG, useUnmergedTree = true)
            .assertCountEquals(0)
    }

    @Test
    fun when_scrubDateLabelIsLongAndGraphIsNarrow_should_renderWithoutLayoutFailure() {
        val priceHistory = persistentListOf(
            PriceEntry(
                price = BigDecimal("100.00"),
                timestamp = 1L,
                formattedDate = "Yesterday 23:13"
            ),
            PriceEntry(
                price = BigDecimal("110.00"),
                timestamp = 2L,
                formattedDate = "Yesterday 23:14"
            )
        )

        composeTestRule.setContent {
            AppTheme {
                ScrubPriceGraph(
                    priceHistory = priceHistory,
                    priceChangePercentage = Percentage("10.00"),
                    isGraphAnimated = false,
                    onScrub = {},
                    modifier = Modifier
                        .width(40.dp)
                        .height(180.dp)
                )
            }
        }

        composeTestRule.onNodeWithTag(SCRUB_CANVAS_TAG, useUnmergedTree = true)
            .performTouchInput {
                down(Offset(x = width / 2f, y = height / 2f))
            }

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag(SCRUB_DATE_LABEL_TAG, useUnmergedTree = true)
            .assertIsDisplayed()
    }

    private fun assertNodeHasDrawnPixels(tag: String) {
        val pixelMap = composeTestRule
            .onNodeWithTag(tag, useUnmergedTree = true)
            .captureToImage()
            .toPixelMap()

        val backgroundColor = pixelMap[pixelMap.width - 1, 0]
        var hasDrawnPixel = false
        for (x in 0 until pixelMap.width) {
            for (y in 0 until pixelMap.height) {
                if (pixelMap[x, y] != backgroundColor) {
                    hasDrawnPixel = true
                    break
                }
            }
            if (hasDrawnPixel) break
        }

        assertThat(hasDrawnPixel).isTrue()
    }

    private fun assertNodeTopRowIsUniform(tag: String) {
        val pixelMap = composeTestRule
            .onNodeWithTag(tag, useUnmergedTree = true)
            .captureToImage()
            .toPixelMap()
        val backgroundColor = pixelMap[pixelMap.width - 1, 0]

        for (x in 0 until pixelMap.width) {
            assertThat(pixelMap[x, 0]).isEqualTo(backgroundColor)
        }
    }

    private companion object {
        const val CALLER_STATIC_GRAPH_TAG = "callerStaticPriceGraph"
        const val SCRUB_DATE_LABEL_TAG = "scrubPriceGraphDateLabel"
        const val SCRUB_CANVAS_TAG = "scrubPriceGraphCanvas"
        const val STATIC_CANVAS_TAG = "staticPriceGraphCanvas"

        val sampleStaticPrices = persistentListOf(
            BigDecimal("100.00"),
            BigDecimal("115.00"),
            BigDecimal("110.00"),
            BigDecimal("125.00"),
            BigDecimal("140.00")
        )

        val samplePriceHistory: ImmutableList<PriceEntry> = persistentListOf(
            PriceEntry(price = BigDecimal("100.00"), timestamp = 1L, formattedDate = "01 Jan"),
            PriceEntry(price = BigDecimal("110.00"), timestamp = 2L, formattedDate = "02 Jan"),
            PriceEntry(price = BigDecimal("120.00"), timestamp = 3L, formattedDate = "03 Jan"),
            PriceEntry(price = BigDecimal("130.00"), timestamp = 4L, formattedDate = "04 Jan"),
            PriceEntry(price = BigDecimal("140.00"), timestamp = 5L, formattedDate = "05 Jan")
        )

        val shorterReplacementPriceHistory: ImmutableList<PriceEntry> = persistentListOf(
            PriceEntry(price = BigDecimal("90.00"), timestamp = 11L, formattedDate = "10 Feb"),
            PriceEntry(price = BigDecimal("95.00"), timestamp = 12L, formattedDate = "11 Feb")
        )
    }
}
