package dev.shorthouse.coinwatch.ui.screen.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.shorthouse.coinwatch.R
import dev.shorthouse.coinwatch.data.userPreferences.StartDestination
import dev.shorthouse.coinwatch.ui.component.ErrorState
import dev.shorthouse.coinwatch.ui.component.LoadingIndicator
import dev.shorthouse.coinwatch.ui.previewdata.SettingsUiStatePreviewProvider
import dev.shorthouse.coinwatch.ui.screen.settings.component.SettingsItem
import dev.shorthouse.coinwatch.ui.screen.settings.component.StartDestinationDialog
import dev.shorthouse.coinwatch.ui.theme.AppTheme

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SettingsScreen(
        uiState = uiState,
        onUpdateStartDestination = { viewModel.updateStartDestination(it) },
        onNavigateUp = onNavigateUp
    )
}

@Composable
fun SettingsScreen(
    uiState: SettingsUiState,
    onUpdateStartDestination: (StartDestination) -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            SettingsTopBar(onNavigateUp = onNavigateUp)
        },
        modifier = modifier.fillMaxSize()
    ) { scaffoldPadding ->
        when {
            uiState.isLoading -> {
                LoadingIndicator(modifier = Modifier.padding(scaffoldPadding))
            }

            uiState.errorMessage != null -> {
                ErrorState(
                    message = stringResource(R.string.error_state_settings),
                    modifier = Modifier.padding(scaffoldPadding)
                )
            }

            else -> {
                SettingsContent(
                    startDestination = uiState.startDestination,
                    onUpdateStartDestination = onUpdateStartDestination,
                    modifier = Modifier.padding(scaffoldPadding)
                )
            }
        }
    }
}

@Composable
fun SettingsContent(
    startDestination: StartDestination,
    onUpdateStartDestination: (StartDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    var isStartDestinationDialogOpen by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        SettingsItem(
            title = stringResource(R.string.settings_title_start_destination),
            subtitle = startDestination.name,
            leadingIcon = Icons.Rounded.Home,
            trailingIcon = Icons.Rounded.ChevronRight,
            onClick = {
                isStartDestinationDialogOpen = true
            }
        )
    }

    if (isStartDestinationDialogOpen) {
        StartDestinationDialog(
            initialSelectedDestination = StartDestination.Favourites,
            onOptionSelected = { onUpdateStartDestination(it) },
            onDismissRequest = { isStartDestinationDialogOpen = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsTopBar(
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.settings_screen),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        },
        navigationIcon = {
            IconButton(onClick = onNavigateUp) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = stringResource(R.string.cd_top_bar_back)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            scrolledContainerColor = MaterialTheme.colorScheme.background
        ),
        modifier = modifier
    )
}

@Preview
@Composable
private fun SettingsScreenPreview(
    @PreviewParameter(SettingsUiStatePreviewProvider::class) uiState: SettingsUiState
) {
    AppTheme {
        SettingsScreen(
            uiState = uiState,
            onUpdateStartDestination = {},
            onNavigateUp = {}
        )
    }
}
