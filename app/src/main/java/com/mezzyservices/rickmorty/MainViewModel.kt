package com.mezzyservices.rickmorty

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mezzyservices.rickmorty.data.repository.EpisodeRepository
import com.mezzyservices.rickmorty.usecases.GetEpisodeListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val getEpisodeListUseCase: GetEpisodeListUseCase
): ViewModel() {

    val command = MutableLiveData<Command>()

    fun getEpisodeList() {
        viewModelScope.launch {
            try {
                getEpisodeListUseCase.getEpisodeList()
            } catch (e: Exception) {
                Log.e("Episode List", e.message ?: "sin info")
            }
        }
    }


    sealed class Command {
        class Success(): Command()
        class Error(message: String): Command()
    }

}