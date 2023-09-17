package dev.shorthouse.coinwatch.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val darkColorScheme = darkColorScheme(
    background = dark_background,
    onBackground = dark_onBackground,
    surface = dark_surface,
    onSurface = dark_onSurface,
    onSurfaceVariant = dark_onSurfaceVariant
)

@Composable
fun AppTheme(
    systemUiController: SystemUiController = rememberSystemUiController(),
    content: @Composable () -> Unit
) {
    DisposableEffect(systemUiController) {
        systemUiController.setSystemBarsColor(
            color = darkColorScheme.background
        )

        onDispose {}
    }

    MaterialTheme(
        colorScheme = darkColorScheme,
        shapes = AppShapes,
        typography = AppTypography,
        content = content
    )
}
