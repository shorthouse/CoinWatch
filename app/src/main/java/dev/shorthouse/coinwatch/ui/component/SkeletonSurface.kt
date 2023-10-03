package dev.shorthouse.coinwatch.ui.component

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.shorthouse.coinwatch.ui.theme.AppTheme

@Composable
fun SkeletonSurface(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
    content: @Composable () -> Unit = {}
) {
    Box(
        modifier = modifier
            .clip(shape)
            .shimmerFadeEffect()
    ) {
        content()
    }
}

private fun Modifier.shimmerFadeEffect(): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "infiniteTransition")

    val translationAnim by transition.animateColor(
        initialValue = MaterialTheme.colorScheme.surface.copy(alpha = 0.4f),
        targetValue = MaterialTheme.colorScheme.surface,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 300,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmerAnimation"
    )

    background(
        color = translationAnim
    )
}

@Preview
@Composable
private fun SkeletonSurfacePreview() {
    AppTheme {
        SkeletonSurface {
            Box(modifier = Modifier.size(100.dp))
        }
    }
}
