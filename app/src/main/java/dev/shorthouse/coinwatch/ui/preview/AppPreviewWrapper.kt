package dev.shorthouse.coinwatch.ui.preview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.PreviewWrapperProvider
import coil3.ColorImage
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImagePreviewHandler
import coil3.compose.LocalAsyncImagePreviewHandler
import dev.shorthouse.coinwatch.ui.theme.AppTheme

class AppThemePreviewWrapper : PreviewWrapperProvider {

    @Composable
    override fun Wrap(content: @Composable () -> Unit) {
        AppTheme(content = content)
    }
}

@OptIn(ExperimentalCoilApi::class)
class CoilPreviewWrapper : PreviewWrapperProvider {

    @Composable
    override fun Wrap(content: @Composable () -> Unit) {
        val previewImageHandler = remember {
            AsyncImagePreviewHandler {
                ColorImage(Color.Gray.toArgb())
            }
        }

        CompositionLocalProvider(
            LocalAsyncImagePreviewHandler provides previewImageHandler,
            content = content
        )
    }
}

class AppPreviewWrapper : PreviewWrapperProvider {

    private val themeWrapper = AppThemePreviewWrapper()
    private val coilWrapper = CoilPreviewWrapper()

    @Composable
    override fun Wrap(content: @Composable () -> Unit) {
        themeWrapper.Wrap {
            coilWrapper.Wrap(content = content)
        }
    }
}
