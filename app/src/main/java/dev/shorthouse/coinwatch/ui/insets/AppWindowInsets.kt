package dev.shorthouse.coinwatch.ui.insets

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.runtime.Composable

internal object AppWindowInsets {
    val horizontalContent: WindowInsets
        @Composable
        get() = WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal)

    val horizontalAndBottomContent: WindowInsets
        @Composable
        get() = WindowInsets.safeDrawing.only(
            WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal
        )
}
