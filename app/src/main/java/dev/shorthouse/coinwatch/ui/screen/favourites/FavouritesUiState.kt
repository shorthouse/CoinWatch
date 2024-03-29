package dev.shorthouse.coinwatch.ui.screen.favourites

import dev.shorthouse.coinwatch.data.source.local.model.FavouriteCoin
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class FavouritesUiState(
    val favouriteCoins: ImmutableList<FavouriteCoin> = persistentListOf(),
    val isFavouritesCondensed: Boolean = false,
    val isRefreshing: Boolean = false,
    val isFavouriteCoinsEmpty: Boolean = true,
    val isLoading: Boolean = false,
    val errorMessageIds: List<Int> = persistentListOf()
)
