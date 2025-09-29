package com.mezzyservices.rickmorty.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mezzyservices.rickmorty.data.model.Info


@Dao
interface InfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(info: Info)

    @Query("SELECT * FROM info")
    suspend fun get(): Info

    @Query("DELETE FROM info")
    suspend fun delete()
}