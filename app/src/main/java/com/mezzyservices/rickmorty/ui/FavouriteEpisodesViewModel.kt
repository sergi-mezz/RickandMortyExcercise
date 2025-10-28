package com.mezzyservices.rickmorty.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mezzyservices.rickmorty.data.local.FavouriteCharacter
import com.mezzyservices.rickmorty.data.repository.FavouriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FavouriteEpisodesViewModel @Inject constructor(
    val favouriteRepository: FavouriteRepository
): ViewModel() {


    val command = MutableLiveData<Command>(Command.Loading)
    var favouriteCharacterList = listOf<FavouriteCharacter>()

    fun getEpisodes() {

        command.value = Command.Loading

        viewModelScope.launch {

            try {
                favouriteCharacterList = favouriteRepository.getAll()
                command.postValue(Command.Success)
            } catch (e: Exception) {
                command.postValue(Command.Error(e.message ?: "sin info"))
            }
        }
    }

    fun deleteFavourite(character: FavouriteCharacter) {

        viewModelScope.launch {
            try {
                favouriteRepository.remove(character)
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