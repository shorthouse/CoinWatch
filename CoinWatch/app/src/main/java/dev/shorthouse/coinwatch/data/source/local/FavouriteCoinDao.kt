package dev.shorthouse.coinwatch.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.shorthouse.coinwatch.data.source.local.model.FavouriteCoin
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteCoinDao {
    @Query("SELECT * FROM FavouriteCoin")
    fun getFavouriteCoins(): Flow<List<FavouriteCoin>>

    @Query("SELECT COUNT(1) FROM FavouriteCoin WHERE id = :coinId")
    fun isCoinFavourite(coinId: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favouriteCoin: FavouriteCoin)

    @Delete
    suspend fun delete(favouriteCoin: FavouriteCoin)
}
