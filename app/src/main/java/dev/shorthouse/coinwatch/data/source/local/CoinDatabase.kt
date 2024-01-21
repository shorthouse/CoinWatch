package dev.shorthouse.coinwatch.data.source.local

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RenameTable
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.AutoMigrationSpec
import dev.shorthouse.coinwatch.data.source.local.converters.ImmutableListTypeConverter
import dev.shorthouse.coinwatch.data.source.local.converters.PercentageTypeConverter
import dev.shorthouse.coinwatch.data.source.local.converters.PriceTypeConverter
import dev.shorthouse.coinwatch.data.source.local.model.CachedCoin
import dev.shorthouse.coinwatch.data.source.local.model.FavouriteCoinId

@Database(
    version = 3,
    entities = [FavouriteCoinId::class, CachedCoin::class],
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3, spec = CoinDatabase.Migration2to3::class)
    ]
)
@TypeConverters(
    PriceTypeConverter::class,
    PercentageTypeConverter::class,
    ImmutableListTypeConverter::class
)
abstract class CoinDatabase : RoomDatabase() {
    abstract fun favouriteCoinDao(): FavouriteCoinDao
    abstract fun cachedCoinDao(): CachedCoinDao

    @RenameTable(fromTableName = "FavouriteCoin", toTableName = "FavouriteCoinId")
    internal class Migration2to3 : AutoMigrationSpec
}
