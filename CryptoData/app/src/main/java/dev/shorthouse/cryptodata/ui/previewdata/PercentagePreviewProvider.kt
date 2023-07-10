package dev.shorthouse.cryptodata.ui.previewdata

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import dev.shorthouse.cryptodata.model.Percentage
import java.math.BigDecimal

class PercentagePreviewProvider : PreviewParameterProvider<Percentage> {
    override val values = sequenceOf(
        Percentage(BigDecimal("0.42")),
        Percentage(BigDecimal("-0.57")),
        Percentage(BigDecimal("0.0"))
    )
}
