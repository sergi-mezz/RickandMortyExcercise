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
import com.mezzyservices.rickmorty.data.model.Info
import com.mezzyservices.rickmorty.data.remote.RickMortyApi
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class CharacterRemoteMediator(
    private val database: AppDatabase,
    private val apiService: RickMortyApi
) : RemoteMediator<Int, Character>() {

    val characterDao = database.characterDao()
    val pageInfoDao = database.infoDao()

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Character>
    ): MediatorResult {
        return try {

            val loadKey = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND ->
                    return MediatorResult.Success(endOfPaginationReached = false)
                LoadType.APPEND -> {
                    val page = database.withTransaction {
                        pageInfoDao.get()
                    }

                    if (page.next == null) {
                        return MediatorResult.Success(
                            endOfPaginationReached = true
                        )
                    }

                    page.next.substringAfterLast("=").toInt()
                }
            }

            val response = apiService.getCharacters(loadKey)

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    pageInfoDao.delete()
                    characterDao.clearAll()
                }


                pageInfoDao.insertOrReplace(response.info)
                characterDao.insertAll(response.results)
            }

            MediatorResult.Success(
                endOfPaginationReached = response.info.next.isNullOrEmpty()
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        } catch (e: Exception) {
            val x = e.message
            MediatorResult.Error(e)
        }
    }
}