package dev.shorthouse.coinwatch.ui.screen.settings.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.shorthouse.coinwatch.data.userPreferences.StartDestination
import dev.shorthouse.coinwatch.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartDestinationDialog(
    initialSelectedDestination: StartDestination,
    onDismissRequest: () -> Unit,
    onOptionSelected: (StartDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    val startDestinationOptions = StartDestination.values().toList()
    val currentSelectedDestination = remember { mutableStateOf(initialSelectedDestination) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.medium
            )
            .padding(20.dp)
    ) {
        Column {
            Text(
                text = "Choose start destination",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(Modifier.height(16.dp))

            Column(modifier = Modifier.selectableGroup()) {
                startDestinationOptions.forEach { startDestinationOption ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.extraSmall)
                            .selectable(
                                selected = startDestinationOption == initialSelectedDestination,
                                role = Role.RadioButton,
                                onClick = {
                                    currentSelectedDestination.value = startDestinationOption
                                    onOptionSelected(startDestinationOption)
                                    onDismissRequest()
                                }
                            )
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        RadioButton(
                            selected = startDestinationOption == initialSelectedDestination,
                            onClick = null
                        )

                        Spacer(Modifier.width(8.dp))

                        Text(
                            text = startDestinationOption.name,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun SettingsRadioDialogPreview() {
    AppTheme {
        StartDestinationDialog(
            initialSelectedDestination = StartDestination.Favourites,
            onOptionSelected = {},
            onDismissRequest = {}
        )
    }
}
