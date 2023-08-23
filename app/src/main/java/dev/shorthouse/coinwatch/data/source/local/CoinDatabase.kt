package dev.shorthouse.coinwatch.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.shorthouse.coinwatch.data.source.local.model.FavouriteCoin

@Database(entities = [FavouriteCoin::class], version = 1, exportSchema = false)
abstract class CoinDatabase : RoomDatabase() {
    abstract fun favouriteCoinDao(): FavouriteCoinDao
}
