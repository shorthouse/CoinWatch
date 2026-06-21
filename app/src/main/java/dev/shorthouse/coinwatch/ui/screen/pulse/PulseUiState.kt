package dev.shorthouse.coinwatch.ui.screen.pulse

import dev.shorthouse.coinwatch.model.FearGreed
import dev.shorthouse.coinwatch.model.GlobalMarket
import dev.shorthouse.coinwatch.model.Movers
import dev.shorthouse.coinwatch.model.TrendingCoin
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf

data class PulseUiState(
    val fearGreed: FearGreed? = null,
    val globalMarket: GlobalMarket? = null,
    val trendingCoins: ImmutableList<TrendingCoin> = persistentListOf(),
    val movers: Movers? = null,
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val errorMessageIds: Set<Int> = persistentSetOf(),
)
