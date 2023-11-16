package dev.shorthouse.coinwatch.ui.screen.favourites

import dev.shorthouse.coinwatch.model.Coin
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class FavouritesUiState(
    val favouriteCoins: ImmutableList<Coin> = persistentListOf(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
