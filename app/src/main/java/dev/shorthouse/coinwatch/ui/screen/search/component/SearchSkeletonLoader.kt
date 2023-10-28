package dev.shorthouse.coinwatch.ui.screen.search.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.shorthouse.coinwatch.ui.theme.AppTheme

@Composable
fun SearchSkeletonLoader(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize())
}

@Composable
@Preview
private fun SearchSkeletonLoaderPreview() {
    AppTheme {
        SearchSkeletonLoader()
    }
}
