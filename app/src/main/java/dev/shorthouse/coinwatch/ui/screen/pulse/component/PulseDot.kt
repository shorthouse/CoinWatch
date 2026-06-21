package dev.shorthouse.coinwatch.ui.screen.pulse.component

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import dev.shorthouse.coinwatch.model.FearGreedMoodBand
import dev.shorthouse.coinwatch.ui.preview.AppPreviewWrapper
import dev.shorthouse.coinwatch.ui.theme.FearAmber
import dev.shorthouse.coinwatch.ui.theme.GreedLightGreen
import dev.shorthouse.coinwatch.ui.theme.NegativeRed
import dev.shorthouse.coinwatch.ui.theme.PositiveGreen
import dev.shorthouse.coinwatch.ui.theme.ZeroWhite

@Composable
fun PulseDot(
    moodBand: FearGreedMoodBand?,
    modifier: Modifier = Modifier,
) {
    val dotVisible = moodBand != null
    val dotScale by animateFloatAsState(
        targetValue = if (dotVisible) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "pulseDotScale"
    )
    val dotAlpha by animateFloatAsState(
        targetValue = if (dotVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "pulseDotAlpha"
    )

    Box(
        modifier = modifier
            .size(8.dp)
            .graphicsLayer {
                scaleX = dotScale
                scaleY = dotScale
                alpha = dotAlpha
            }
            .clip(CircleShape)
            .background(
                when (moodBand) {
                    FearGreedMoodBand.ExtremeFear -> NegativeRed
                    FearGreedMoodBand.Fear -> FearAmber
                    FearGreedMoodBand.Neutral -> ZeroWhite
                    FearGreedMoodBand.Greed -> GreedLightGreen
                    FearGreedMoodBand.ExtremeGreed -> PositiveGreen
                    null -> Color.Transparent
                }
            )
    )
}

@Preview
@PreviewWrapper(wrapper = AppPreviewWrapper::class)
@Composable
private fun PulseDotPreview() {
    PulseDot(moodBand = FearGreedMoodBand.Greed)
}
