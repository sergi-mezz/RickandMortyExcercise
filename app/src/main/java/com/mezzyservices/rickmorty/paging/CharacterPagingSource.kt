package com.mezzyservices.rickmorty.paging

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mezzyservices.rickmorty.data.remote.RickMortyApi
import com.mezzyservices.rickmorty.data.model.Character
import java.io.IOException

class CharacterPagingSource(private val apiService: RickMortyApi) : PagingSource<Int, Character>() {
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {

        return try {
            val page = params.key ?: 1
            val response = apiService.getCharacters(page)

            LoadResult.Page(
                data = response.results,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (page < response.info.pages!!) page + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        } catch (e: IOException) {
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Character>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}