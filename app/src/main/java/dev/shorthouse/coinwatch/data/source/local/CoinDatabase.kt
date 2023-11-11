package dev.shorthouse.coinwatch.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.shorthouse.coinwatch.data.source.local.converters.ImmutableListTypeConverter
import dev.shorthouse.coinwatch.data.source.local.converters.PercentageTypeConverter
import dev.shorthouse.coinwatch.data.source.local.converters.PriceTypeConverter
import dev.shorthouse.coinwatch.data.source.local.model.CachedCoin
import dev.shorthouse.coinwatch.data.source.local.model.FavouriteCoin

@Database(
    version = 1,
    entities = [FavouriteCoin::class, CachedCoin::class]
)
@TypeConverters(
    PriceTypeConverter::class,
    PercentageTypeConverter::class,
    ImmutableListTypeConverter::class
)
abstract class CoinDatabase : RoomDatabase() {
    abstract fun favouriteCoinDao(): FavouriteCoinDao
    abstract fun cachedCoinDao(): CachedCoinDao
}
