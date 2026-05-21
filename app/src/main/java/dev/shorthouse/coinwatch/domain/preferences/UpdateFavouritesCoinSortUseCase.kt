package dev.shorthouse.coinwatch.domain.preferences

import dev.shorthouse.coinwatch.data.source.local.datastore.common.CoinSort
import dev.shorthouse.coinwatch.data.source.local.datastore.favourites.FavouritesPreferencesRepository
import javax.inject.Inject

class UpdateFavouritesCoinSortUseCase @Inject constructor(
    private val favouritesPreferencesRepository: FavouritesPreferencesRepository,
) {
    suspend operator fun invoke(coinSort: CoinSort) {
        updateFavouritesCoinSort(coinSort = coinSort)
    }

    private suspend fun updateFavouritesCoinSort(coinSort: CoinSort) {
        favouritesPreferencesRepository.updateCoinSort(coinSort = coinSort)
    }
}
