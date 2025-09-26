package com.mezzyservices.rickmorty.ui

import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.mezzyservices.rickmorty.paging.CharacterRemoteMediator
import com.mezzyservices.rickmorty.data.local.AppDatabase
import com.mezzyservices.rickmorty.data.remote.RickMortyApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filter
import javax.inject.Inject

@HiltViewModel
class CharacterListViewModel @Inject constructor(
    val rickMortyApi: RickMortyApi,
    val database: AppDatabase
) : ViewModel() {

    @OptIn(ExperimentalPagingApi::class)
    val characterListFlow = Pager(
        config = PagingConfig(20),
        remoteMediator = CharacterRemoteMediator(database, rickMortyApi),
        pagingSourceFactory = { database.characterDao().pagingSource() }
    ).flow


}