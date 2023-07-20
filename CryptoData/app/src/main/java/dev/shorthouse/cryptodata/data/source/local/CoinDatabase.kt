package dev.shorthouse.cryptodata.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.shorthouse.cryptodata.data.source.local.model.FavouriteCoin

@Database(entities = [FavouriteCoin::class], version = 1, exportSchema = false)
abstract class CoinDatabase : RoomDatabase() {
    abstract fun favouriteCoinDao(): FavouriteCoinDao
}
