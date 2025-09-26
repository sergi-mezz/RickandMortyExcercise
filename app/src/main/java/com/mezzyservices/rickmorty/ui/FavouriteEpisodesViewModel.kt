package com.mezzyservices.rickmorty.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mezzyservices.rickmorty.data.model.Episode
import com.mezzyservices.rickmorty.data.repository.EpisodeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FavouriteEpisodesViewModel @Inject constructor(
    val episodeRepository: EpisodeRepository
): ViewModel() {


    val command = MutableLiveData<Command>(Command.Loading)
    var favouriteEpisodeList = listOf<Episode>()

    fun getEpisodes() {

        command.value = Command.Loading

        viewModelScope.launch {

            try {
                favouriteEpisodeList = episodeRepository.getFavouriteEpisodes()
                command.postValue(Command.Success)
            } catch (e: Exception) {
                command.postValue(Command.Error(e.message ?: "sin info"))
            }
        }
    }

    sealed class Command {

        object Success : Command()
        class Error(message: String): Command()
        object Loading: Command()

    }


}