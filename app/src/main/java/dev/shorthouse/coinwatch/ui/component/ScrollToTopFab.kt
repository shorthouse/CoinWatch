package dev.shorthouse.coinwatch.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardDoubleArrowUp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.shorthouse.coinwatch.R
import androidx.compose.ui.tooling.preview.PreviewWrapper
import dev.shorthouse.coinwatch.ui.preview.AppPreviewWrapper

@Composable
fun ScrollToTopFab(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = 12.dp
        ),
        content = {
            Icon(
                imageVector = Icons.Rounded.KeyboardDoubleArrowUp,
                contentDescription = stringResource(R.string.cd_list_scroll_top)
            )
        },
        modifier = modifier
    )
}

@Preview(showBackground = true)
@PreviewWrapper(wrapper = AppPreviewWrapper::class)
@Composable
private fun ScrollToTopFabPreview() {
    ScrollToTopFab(onClick = {})

}
