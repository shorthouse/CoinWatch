package dev.shorthouse.coinwatch.data.source.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.Price
import java.math.BigDecimal
import kotlinx.collections.immutable.ImmutableList

@Entity
data class FavouriteCoin(
    @PrimaryKey
    val id: String,
    val name: String,
    val symbol: String,
    val imageUrl: String,
    val currentPrice: Price,
    val priceChangePercentage24h: Percentage,
    val prices24h: ImmutableList<BigDecimal>
)
