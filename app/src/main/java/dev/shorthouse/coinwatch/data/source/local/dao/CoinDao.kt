package dev.shorthouse.coinwatch.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import dev.shorthouse.coinwatch.data.source.local.model.Coin
import kotlinx.coroutines.flow.Flow

@Dao
interface CoinDao {
    @Query("SELECT * FROM Coin")
    fun getCoins(): Flow<List<Coin>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCoins(coins: List<Coin>)

    @Query("DELETE FROM Coin")
    fun deleteAllCoins()

    @Transaction
    fun updateCoins(coins: List<Coin>) {
        deleteAllCoins()
        insertCoins(coins)
    }
}
