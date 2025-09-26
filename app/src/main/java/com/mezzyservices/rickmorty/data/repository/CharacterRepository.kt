package com.mezzyservices.rickmorty.data.repository

import com.mezzyservices.rickmorty.data.local.CharacterDao
import com.mezzyservices.rickmorty.data.model.Character
import com.mezzyservices.rickmorty.data.remote.RickMortyApi
import javax.inject.Inject

class CharacterRepository @Inject constructor(
    private val rickMortyApi: RickMortyApi,
    private val characterDao: CharacterDao
) {

    suspend fun getCharacter(characterId: Int): Character {

        val localCharacter = characterDao.get(characterId)
        return localCharacter!!

    }
}