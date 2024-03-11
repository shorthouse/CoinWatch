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
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.shorthouse.coinwatch.R
import dev.shorthouse.coinwatch.data.preferences.global.StartScreen
import dev.shorthouse.coinwatch.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartScreenDialog(
    initialSelectedDestination: StartScreen,
    onUpdateStartScreen: (StartScreen) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    val startScreenOptions = StartScreen.entries

    BasicAlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.large
            )
    ) {
        Column {
            Text(
                text = stringResource(R.string.start_screen_title),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(20.dp)
            )

            Column(modifier = Modifier.selectableGroup()) {
                startScreenOptions.forEach { startScreenOption ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .selectable(
                                selected = startScreenOption == initialSelectedDestination,
                                role = Role.RadioButton,
                                onClick = {
                                    onUpdateStartScreen(startScreenOption)
                                    onDismissRequest()
                                },
                            )
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 16.dp)
                    ) {
                        RadioButton(
                            selected = startScreenOption == initialSelectedDestination,
                            onClick = null
                        )

                        Spacer(Modifier.width(16.dp))

                        Text(
                            text = stringResource(startScreenOption.nameId),
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
@Preview
private fun StartScreenDialogPreview() {
    AppTheme {
        StartScreenDialog(
            initialSelectedDestination = StartScreen.Favourites,
            onUpdateStartScreen = {},
            onDismissRequest = {}
        )
    }
}
