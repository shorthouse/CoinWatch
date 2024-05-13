package dev.shorthouse.coinwatch.ui.screen.market

import dev.shorthouse.coinwatch.data.source.local.preferences.common.CoinSort
import dev.shorthouse.coinwatch.data.source.local.database.model.Coin
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.ui.model.TimeOfDay
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class MarketUiState(
    val coins: ImmutableList<Coin> = persistentListOf(),
    val timeOfDay: TimeOfDay = TimeOfDay.Morning,
    val marketCapChangePercentage24h: Percentage? = null,
    val coinSort: CoinSort = CoinSort.MarketCap,
    val isRefreshing: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessageIds: List<Int> = persistentListOf()
)
