package com.mezzyservices.rickmorty.usecases

import com.mezzyservices.rickmorty.data.repository.CharacterRepository
import javax.inject.Inject

class GetCharacterUseCase @Inject constructor(
    val repository: CharacterRepository
) {

    suspend fun getCharacter(characterId: Int) = repository.getCharacter(characterId)
}