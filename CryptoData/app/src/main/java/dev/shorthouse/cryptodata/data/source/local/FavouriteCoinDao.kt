package dev.shorthouse.cryptodata.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.shorthouse.cryptodata.data.source.local.model.FavouriteCoin

@Dao
interface FavouriteCoinDao {
    @Query("SELECT * FROM favouritecoin")
    suspend fun getFavouriteCoins(): List<FavouriteCoin>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favouriteCoin: FavouriteCoin)

    @Delete
    suspend fun delete(favouriteCoin: FavouriteCoin)
}
