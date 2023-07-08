package dev.shorthouse.cryptodata.ui.previewdata

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import dev.shorthouse.cryptodata.model.Percentage

class PercentagePreviewProvider : PreviewParameterProvider<Percentage> {
    override val values = sequenceOf(
        Percentage(0.42),
        Percentage(-0.57),
        Percentage(0.0)
    )
}
