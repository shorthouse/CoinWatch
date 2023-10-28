package dev.shorthouse.coinwatch.ui.screen.search

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.shorthouse.coinwatch.R
import dev.shorthouse.coinwatch.model.SearchCoin
import dev.shorthouse.coinwatch.ui.component.ErrorState
import dev.shorthouse.coinwatch.ui.previewdata.SearchUiStatePreviewProvider
import dev.shorthouse.coinwatch.ui.screen.search.component.SearchEmptyState
import dev.shorthouse.coinwatch.ui.screen.search.component.SearchListItem
import dev.shorthouse.coinwatch.ui.screen.search.component.SearchSkeletonLoader
import dev.shorthouse.coinwatch.ui.theme.AppTheme
import kotlinx.collections.immutable.ImmutableList

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    onNavigateDetails: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SearchScreen(
        uiState = uiState,
        searchQuery = viewModel.searchQuery,
        onSearchQueryChange = { viewModel.updateSearchQuery(it) },
        onCoinClick = { coin ->
            onNavigateDetails(coin.id)
        },
        onRefresh = { viewModel.initialiseUiState() }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SearchScreen(
    uiState: SearchUiState,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onCoinClick: (SearchCoin) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    SearchBar(
        query = searchQuery,
        onQueryChange = onSearchQueryChange,
        onSearch = { keyboardController?.hide() },
        placeholder = {
            Text(
                text = stringResource(R.string.search_coins_hint),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        leadingIcon = {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = stringResource(R.string.cd_top_bar_back)
                )
            }
        },
        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                IconButton(onClick = { onSearchQueryChange("") }) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        tint = MaterialTheme.colorScheme.onSurface,
                        contentDescription = stringResource(R.string.cd_clear_search)
                    )
                }
            }
        },
        content = {
            when (uiState) {
                is SearchUiState.Success -> {
                    SearchContent(
                        searchResults = uiState.searchResults,
                        queryHasNoResults = uiState.queryHasNoResults,
                        onCoinClick = onCoinClick
                    )
                }

                is SearchUiState.Error -> {
                    ErrorState(
                        message = uiState.message,
                        onRetry = onRefresh,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                }

                is SearchUiState.Loading -> {
                    SearchSkeletonLoader()
                }
            }
        },
        colors = SearchBarDefaults.colors(
            containerColor = MaterialTheme.colorScheme.background,
            dividerColor = MaterialTheme.colorScheme.surface,
            inputFieldColors = TextFieldDefaults.colors(
                cursorColor = MaterialTheme.colorScheme.onSurface,
                focusedIndicatorColor = MaterialTheme.colorScheme.surface,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.surface,
                disabledIndicatorColor = MaterialTheme.colorScheme.surface,
                errorIndicatorColor = MaterialTheme.colorScheme.surface
            )
        ),
        enabled = true,
        active = true,
        onActiveChange = {},
        tonalElevation = 0.dp,
        modifier = modifier.fillMaxSize()
    )
}

@Composable
fun SearchContent(
    searchResults: ImmutableList<SearchCoin>,
    queryHasNoResults: Boolean,
    onCoinClick: (SearchCoin) -> Unit,
    modifier: Modifier = Modifier
) {
    if (queryHasNoResults) {
        SearchEmptyState(
            modifier = modifier
                .fillMaxSize()
                .padding(12.dp)
        )
    } else {
        LazyColumn(
            contentPadding = PaddingValues(12.dp),
            modifier = modifier.fillMaxSize()
        ) {
            items(
                count = searchResults.size,
                key = { index -> searchResults[index].id },
                itemContent = { index ->
                    val searchCoin = searchResults[index]

                    val cardShape = when {
                        searchResults.size == 1 -> MaterialTheme.shapes.medium

                        index == 0 -> MaterialTheme.shapes.medium.copy(
                            bottomStart = CornerSize(0.dp),
                            bottomEnd = CornerSize(0.dp)
                        )

                        index == searchResults.lastIndex ->
                            MaterialTheme.shapes.medium.copy(
                                topStart = CornerSize(0.dp),
                                topEnd = CornerSize(0.dp)
                            )

                        else -> RoundedCornerShape(0.dp)
                    }

                    SearchListItem(
                        searchCoin = searchCoin,
                        onCoinClick = onCoinClick,
                        cardShape = cardShape
                    )
                }
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun SearchScreenPreview(
    @PreviewParameter(SearchUiStatePreviewProvider::class) uiState: SearchUiState
) {
    AppTheme {
        SearchScreen(
            uiState = uiState,
            searchQuery = "",
            onSearchQueryChange = {},
            onCoinClick = {},
            onRefresh = {}
        )
    }
}
