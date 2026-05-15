package dev.shorthouse.coinwatch.ui.previewdata

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import dev.shorthouse.coinwatch.model.Percentage

class PercentagePreviewProvider : PreviewParameterProvider<Percentage> {
    override val values = sequenceOf(
        Percentage("0.42"),
        Percentage("-0.57"),
        Percentage("0.00")
    )
}
