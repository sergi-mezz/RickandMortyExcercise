package com.mezzyservices.rickmorty.data.repository

import com.mezzyservices.rickmorty.data.local.FavouriteCharacter
import com.mezzyservices.rickmorty.data.local.FavouriteDao

class FavouriteRepository(
    val favouriteDao: FavouriteDao
) {
    suspend fun add(character: FavouriteCharacter) = favouriteDao.add(character)

    suspend fun isFavourite(id: Int) = favouriteDao.get(id)?.let { true } ?: false
    suspend fun remove(character: FavouriteCharacter) = favouriteDao.remove(character)

    suspend fun getAll() = favouriteDao.getAll()
}
