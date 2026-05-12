package dev.shorthouse.coinwatch.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AttachMoney
import androidx.compose.material.icons.rounded.CurrencyBitcoin
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.tools.screenshot.PreviewTest
import dev.shorthouse.coinwatch.ui.theme.AppTheme

@PreviewTest
@Preview(showBackground = true)
@Composable
fun BottomSheetOptionScreenshotTest() {
    AppTheme {
        Surface(
            color = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                BottomSheetOption(
                    icon = Icons.Rounded.CurrencyBitcoin,
                    label = "Bitcoin",
                    isSelected = true,
                    onSelected = {},
                )

                Spacer(Modifier.height(8.dp))

                BottomSheetOption(
                    icon = Icons.Rounded.AttachMoney,
                    label = "USD",
                    isSelected = false,
                    onSelected = {},
                )
            }
        }
    }
}
