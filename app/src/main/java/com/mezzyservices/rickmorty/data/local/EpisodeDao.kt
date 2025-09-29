package com.mezzyservices.rickmorty.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.mezzyservices.rickmorty.data.model.Episode

@Dao
interface EpisodeDao {

    @Query("SELECT * FROM episode")
    suspend fun getAll(): List<Episode>

    @Query("SELECT * FROM Episode WHERE id IN (:episodes)")
    suspend fun getEpisodesWithCharacter(episodes: List<Int>): List<Episode>

    @Insert
    suspend fun insertAll(episodes: List<Episode>)

    @Update
    suspend fun update(episode: Episode)
}