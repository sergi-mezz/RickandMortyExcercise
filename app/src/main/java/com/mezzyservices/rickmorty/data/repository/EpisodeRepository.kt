package com.mezzyservices.rickmorty.data.repository

import android.util.Log
import com.mezzyservices.rickmorty.data.local.EpisodeDao
import com.mezzyservices.rickmorty.data.model.Episode
import com.mezzyservices.rickmorty.data.remote.RickMortyApi
import javax.inject.Inject

class EpisodeRepository @Inject constructor(
    val episodeDao: EpisodeDao,
    val rickMortyApi: RickMortyApi
){

    suspend fun getEpisodeList() {

        var localEpisodes = listOf<Episode>()

        try {
            localEpisodes = episodeDao.getAll()
        } catch (e: Exception) {
            Log.e("Episode Repository", e.message ?: "sin info")
        }

        if(localEpisodes.isEmpty()) {

            try {
                var page = 1
                while(true) {
                    val episodes = rickMortyApi.getEpisodes(page)
                    episodeDao.insertAll(episodes.results)
                    if(episodes.info.next.isNullOrEmpty())
                        break
                    page++
                }
            } catch (e: Exception) {
                Log.e("EpisodeRepository", e.message ?: "sin info")
            }

        }
    }
    suspend fun getEpisodesByCharacter(episodesIdList: List<Int>) = episodeDao.getEpisodesWithCharacter(episodesIdList)

    suspend fun addFavouriteEpisode(episode: Episode) {

        val favourite = episode.isFavourite
        episodeDao.update(episode.apply {
            isFavourite = !favourite
        })
    }
    suspend fun addWatchedEpisode(episode: Episode) {

        val watched = episode.alreadyWatched
        episodeDao.update(episode.apply {
            alreadyWatched = !watched
        })
    }

    suspend fun getFavouriteEpisodes() = episodeDao.getFavouriteEpisodes()
}