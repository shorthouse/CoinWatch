package dev.shorthouse.coinwatch.data.source.local.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.Price

@Entity
data class Coin(
    @PrimaryKey
    val id: String,
    val name: String,
    val symbol: String,
    val imageUrl: String,
    val currentPrice: Price,
    val priceChangePercentage24h: Percentage
)
