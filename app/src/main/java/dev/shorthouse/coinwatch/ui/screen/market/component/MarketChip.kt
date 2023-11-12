package dev.shorthouse.coinwatch.ui.screen.market.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.shorthouse.coinwatch.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketChip(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilterChip(
        label = { Text(text = label) },
        onClick = onClick,
        selected = false,
        trailingIcon = {
            Icon(
                imageVector = Icons.Rounded.ExpandMore,
                tint = MaterialTheme.colorScheme.onBackground,
                contentDescription = stringResource(
                    R.string.top_bar_action_change_currency
                )
            )
        },
        colors = FilterChipDefaults.filterChipColors(
            containerColor = MaterialTheme.colorScheme.surface,
            labelColor = MaterialTheme.colorScheme.onSurface,
            iconColor = MaterialTheme.colorScheme.onSurface
        ),
        border = null,
        modifier = modifier
    )
}
