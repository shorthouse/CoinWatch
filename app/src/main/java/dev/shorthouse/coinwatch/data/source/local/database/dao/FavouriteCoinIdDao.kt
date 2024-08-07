package dev.shorthouse.coinwatch.data.source.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.shorthouse.coinwatch.data.source.local.database.model.FavouriteCoinId
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteCoinIdDao {
    @Query("SELECT * FROM FavouriteCoinId")
    fun getFavouriteCoinIds(): Flow<List<FavouriteCoinId>>

    @Query("SELECT COUNT(1) FROM FavouriteCoinId WHERE id = :coinId")
    fun isCoinFavourite(coinId: String): Flow<Boolean>

    @Query("SELECT COUNT(1) FROM FavouriteCoinId WHERE id = :coinId")
    suspend fun isCoinFavouriteOneShot(coinId: String): Boolean

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favouriteCoinId: FavouriteCoinId)

    @Delete
    suspend fun delete(favouriteCoinId: FavouriteCoinId)
}
