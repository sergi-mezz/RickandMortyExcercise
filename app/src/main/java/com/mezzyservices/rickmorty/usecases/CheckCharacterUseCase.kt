package com.mezzyservices.rickmorty.usecases

import com.mezzyservices.rickmorty.data.local.FavouriteCharacter
import com.mezzyservices.rickmorty.data.repository.FavouriteRepository
import javax.inject.Inject

class CheckCharacterUseCase @Inject constructor(
    val favouriteRepository: FavouriteRepository
) {

    suspend fun checkCharacter(character: FavouriteCharacter, isFavourite: Boolean) {
        if(!isFavourite)
            favouriteRepository.add(character)
        else
            favouriteRepository.remove(character)
    }

    suspend fun isFavourite(id: Int) = favouriteRepository.isFavourite(id)

}