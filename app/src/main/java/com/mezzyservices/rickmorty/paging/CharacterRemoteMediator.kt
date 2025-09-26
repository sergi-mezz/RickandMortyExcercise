package com.mezzyservices.rickmorty.paging

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.mezzyservices.rickmorty.data.local.AppDatabase
import com.mezzyservices.rickmorty.data.model.Character
import com.mezzyservices.rickmorty.data.remote.RickMortyApi
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class CharacterRemoteMediator(
    private val database: AppDatabase,
    private val apiService: RickMortyApi
) : RemoteMediator<Int, Character>() {

    val characterDao = database.characterDao()

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Character>
    ): MediatorResult {
        return try {

            val loadKey = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND ->
                    return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()

                    if (lastItem == null) {
                        return MediatorResult.Success(
                            endOfPaginationReached = true
                        )
                    }

                    lastItem.id?.div(20)?.plus(1) ?: 1
                }
            }

            val response = apiService.getCharacters(loadKey ?: 1)

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    characterDao.clearAll()
                }

                characterDao.insertAll(response.results)
            }

            MediatorResult.Success(
                endOfPaginationReached = response.info.next == null
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}