package dev.shorthouse.coinwatch.domain

import dev.shorthouse.coinwatch.data.preferences.common.CoinSort
import dev.shorthouse.coinwatch.data.preferences.favourites.FavouritesPreferencesRepository
import javax.inject.Inject

class UpdateFavouritesCoinSortUseCase @Inject constructor(
    private val favouritesPreferencesRepository: FavouritesPreferencesRepository
) {
    suspend operator fun invoke(coinSort: CoinSort) {
        updateFavouritesCoinSort(coinSort = coinSort)
    }

    private suspend fun updateFavouritesCoinSort(coinSort: CoinSort) {
        favouritesPreferencesRepository.updateCoinSort(coinSort = coinSort)
    }
}
