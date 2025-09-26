package com.mezzyservices.rickmorty.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.mezzyservices.rickmorty.data.model.Character
import com.mezzyservices.rickmorty.data.model.CharacterResponse

@Dao
interface CharacterDao {

    @Query("SELECT * FROM character")
    suspend fun getAll(): List<Character>

    @Query("SELECT * FROM character WHERE id == :id")
    suspend fun get(id: Int): Character?

    @Insert(onConflict = REPLACE)
    suspend fun insertAll(characters: List<Character>)

    @Insert
    suspend fun insert(character: Character)

    @Query("SELECT * FROM character")
    fun pagingSource(): PagingSource<Int, Character>

    @Query("DELETE FROM character")
    suspend fun clearAll()

}