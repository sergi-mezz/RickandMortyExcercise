package com.mezzyservices.rickmorty.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavouriteDao {

    @Query("SELECT * FROM favouritecharacter")
    suspend fun getAll(): List<FavouriteCharacter>

    @Insert
    suspend fun add(favourite: FavouriteCharacter)

    @Delete
    suspend fun remove(favourite: FavouriteCharacter)

    @Query("SELECT * FROM favouritecharacter WHERE id=:id")
    suspend fun get(id: Int): FavouriteCharacter?

}
