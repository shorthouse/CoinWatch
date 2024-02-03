package dev.shorthouse.coinwatch.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import dev.shorthouse.coinwatch.data.source.local.model.FavouriteCoin
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteCoinDao {
    @Query("SELECT * FROM FavouriteCoin")
    fun getFavouriteCoins(): Flow<List<FavouriteCoin>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavouriteCoins(favouriteCoins: List<FavouriteCoin>)

    @Query("DELETE FROM FavouriteCoin")
    fun deleteAllFavouriteCoins()

    @Transaction
    fun updateFavouriteCoins(favouriteCoins: List<FavouriteCoin>) {
        deleteAllFavouriteCoins()
        insertFavouriteCoins(favouriteCoins)
    }
}
