package dev.shorthouse.coinwatch.ui.screen.favourites

import dev.shorthouse.coinwatch.model.Coin
import kotlinx.collections.immutable.ImmutableList

sealed interface FavouritesUiState {
    object Loading : FavouritesUiState
    data class Success(
        val favouriteCoins: ImmutableList<Coin>
    ) : FavouritesUiState

    data class Error(val message: String?) : FavouritesUiState
}
