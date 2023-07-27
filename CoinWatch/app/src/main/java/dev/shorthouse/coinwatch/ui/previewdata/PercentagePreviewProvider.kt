package dev.shorthouse.coinwatch.ui.previewdata

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import dev.shorthouse.coinwatch.model.Percentage
import java.math.BigDecimal

class PercentagePreviewProvider : PreviewParameterProvider<Percentage> {
    override val values = sequenceOf(
        Percentage(BigDecimal("0.42")),
        Percentage(BigDecimal("-0.57")),
        Percentage(BigDecimal("0.00"))
    )
}
