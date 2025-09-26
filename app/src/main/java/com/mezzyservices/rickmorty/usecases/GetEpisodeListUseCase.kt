package com.mezzyservices.rickmorty.usecases

import com.mezzyservices.rickmorty.data.repository.EpisodeRepository
import javax.inject.Inject

class GetEpisodeListUseCase @Inject constructor(
    val repository: EpisodeRepository
) {
    suspend fun getEpisodeList() {
        repository.getEpisodeList()
    }

    suspend fun getEpisodesByCharacter(episodesIdList: List<Int>) = repository.getEpisodesByCharacter(episodesIdList)
}