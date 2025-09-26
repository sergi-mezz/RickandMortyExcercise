package com.mezzyservices.rickmorty.usecases

import com.mezzyservices.rickmorty.data.model.Episode
import com.mezzyservices.rickmorty.data.repository.EpisodeRepository
import javax.inject.Inject


class CheckEpisodeUseCase @Inject constructor(
    val repository: EpisodeRepository
) {

    suspend fun addFavouriteEpisode(episode: Episode) {
        repository.addFavouriteEpisode(episode)
    }

    suspend fun addWatchedEpisode(episode: Episode) {
        repository.addWatchedEpisode(episode)
    }

}