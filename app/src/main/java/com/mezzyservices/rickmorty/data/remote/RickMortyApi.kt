package com.mezzyservices.rickmorty.data.remote

import retrofit2.http.GET
import com.mezzyservices.rickmorty.data.model.CharacterResponse
import com.mezzyservices.rickmorty.data.model.EpisodeResponse
import retrofit2.http.Path
import retrofit2.http.Query


interface RickMortyApi {

    @GET("character")
    suspend fun getCharacters(@Query("page") page: Int) : CharacterResponse

    @GET("episode")
    suspend fun getEpisodes(@Query("page") page: Int): EpisodeResponse
}