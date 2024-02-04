package dev.shorthouse.coinwatch.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import dev.shorthouse.coinwatch.data.source.local.model.CachedCoin
import kotlinx.coroutines.flow.Flow

@Dao
interface CoinDao {
    @Query("SELECT * FROM CachedCoin")
    fun getCoins(): Flow<List<CachedCoin>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCoins(coins: List<CachedCoin>)

    @Query("DELETE FROM CachedCoin")
    fun deleteAllCoins()

    @Transaction
    fun updateCoins(coins: List<CachedCoin>) {
        deleteAllCoins()
        insertCoins(coins)
    }
}
