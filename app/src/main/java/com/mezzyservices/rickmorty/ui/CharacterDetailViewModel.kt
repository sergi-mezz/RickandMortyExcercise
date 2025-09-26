package com.mezzyservices.rickmorty.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mezzyservices.rickmorty.data.model.Character
import com.mezzyservices.rickmorty.data.model.Episode
import com.mezzyservices.rickmorty.usecases.CheckEpisodeUseCase
import com.mezzyservices.rickmorty.usecases.GetCharacterUseCase
import com.mezzyservices.rickmorty.usecases.GetEpisodeListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterDetailViewModel @Inject constructor(
    val getCharacterUseCase: GetCharacterUseCase,
    val getEpisodesUseCase: GetEpisodeListUseCase,
    val checkEpisodeUseCase: CheckEpisodeUseCase
): ViewModel() {

    val command = MutableLiveData<Command>(Command.Loading())
    lateinit var character: Character
    var episodeList = listOf<Episode>()

    fun getCharacter(characterId: Int) {

        command.value = Command.Loading()

        viewModelScope.launch {
            try {
                character = getCharacterUseCase.getCharacter(characterId)
                episodeList = getEpisodesUseCase.getEpisodesByCharacter(character.episode!!.map { it.substringAfterLast("/").toInt() })
                command.postValue(Command.Success())
            } catch (e: Exception) {
                command.postValue(Command.Error())
            }
        }
    }

    fun addFavouriteEpisode(episode: Episode) {
        viewModelScope.launch {
            checkEpisodeUseCase.addFavouriteEpisode(episode)
        }
    }

    fun addWatchedEpisode(episode: Episode) {
        viewModelScope.launch {
            checkEpisodeUseCase.addWatchedEpisode(episode)
        }
    }
    sealed class Command {

        class Success() : Command()
        class Error(): Command()
        class Loading(): Command()

    }


}