package dev.shorthouse.coinwatch.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import dev.shorthouse.coinwatch.data.source.local.model.CachedCoin
import kotlinx.coroutines.flow.Flow

@Dao
interface CachedCoinDao {
    @Query("SELECT * FROM CachedCoin")
    fun getCachedCoins(): Flow<List<CachedCoin>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCachedCoins(coins: List<CachedCoin>)

    @Query("DELETE FROM CachedCoin")
    fun deleteAllCachedCoins()

    @Transaction
    fun refreshCachedCoins(coins: List<CachedCoin>) {
        deleteAllCachedCoins()
        insertCachedCoins(coins)
    }
}
