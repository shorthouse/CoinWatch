package dev.shorthouse.cryptodata.ui.screen.detail.component

import android.graphics.Typeface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.component.lineComponent
import com.patrykandpatrick.vico.compose.component.shapeComponent
import com.patrykandpatrick.vico.compose.dimensions.dimensionsOf
import com.patrykandpatrick.vico.core.component.marker.MarkerComponent
import com.patrykandpatrick.vico.core.component.shape.DashedShape
import com.patrykandpatrick.vico.core.component.shape.ShapeComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.component.shape.cornered.Corner
import com.patrykandpatrick.vico.core.component.shape.cornered.MarkerCorneredShape
import com.patrykandpatrick.vico.core.component.text.textComponent
import com.patrykandpatrick.vico.core.marker.Marker

@Composable
internal fun rememberChartMarker(): Marker {
    val labelBackgroundColor = MaterialTheme.colorScheme.surface
    val labelBackgroundShape = MarkerCorneredShape(Corner.FullyRounded)

    val labelBackground = remember(labelBackgroundColor) {
        ShapeComponent(
            shape = labelBackgroundShape,
            color = labelBackgroundColor.toArgb()
        )
            .setShadow(
                radius = 4f,
                dy = 2f
            )
    }

    val label = textComponent {
        background = labelBackground
        padding = dimensionsOf(
            horizontal = 8.dp,
            vertical = 4.dp
        )
        typeface = Typeface.MONOSPACE
    }

    val indicator = shapeComponent(
        shape = Shapes.pillShape,
        color = Color.Black
    )

    val guideline = lineComponent(
        color = Color.Black,
        thickness = 2.dp,
        shape = DashedShape(
            shape = Shapes.pillShape,
            dashLengthDp = 8f,
            gapLengthDp = 4f
        )
    )

    // Key for changing theme
    return remember(label, indicator) {
        object : MarkerComponent(label, indicator, guideline) {
            init {
                indicatorSizeDp = 10f
            }
        }
    }
}
