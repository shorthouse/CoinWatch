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
import dev.shorthouse.coinwatch.data.source.local.dao.CachedCoinDao
import dev.shorthouse.coinwatch.data.source.local.dao.FavouriteCoinDao
import dev.shorthouse.coinwatch.data.source.local.dao.FavouriteCoinIdDao
import dev.shorthouse.coinwatch.data.source.local.model.CachedCoin
import dev.shorthouse.coinwatch.data.source.local.model.FavouriteCoin
import dev.shorthouse.coinwatch.data.source.local.model.FavouriteCoinId

@Database(
    version = 4,
    entities = [FavouriteCoinId::class, CachedCoin::class, FavouriteCoin::class],
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3, spec = CoinDatabase.Migration2to3::class),
        AutoMigration(from = 3, to = 4)
    ]
)
@TypeConverters(
    PriceTypeConverter::class,
    PercentageTypeConverter::class,
    ImmutableListTypeConverter::class
)
abstract class CoinDatabase : RoomDatabase() {
    abstract fun favouriteCoinIdDao(): FavouriteCoinIdDao
    abstract fun cachedCoinDao(): CachedCoinDao
    abstract fun favouriteCoinDao(): FavouriteCoinDao

    @RenameTable(fromTableName = "FavouriteCoin", toTableName = "FavouriteCoinId")
    internal class Migration2to3 : AutoMigrationSpec
}
