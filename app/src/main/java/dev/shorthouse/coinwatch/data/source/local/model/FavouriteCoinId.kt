package dev.shorthouse.coinwatch.data.source.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavouriteCoinId(
    @PrimaryKey
    val id: String
)
