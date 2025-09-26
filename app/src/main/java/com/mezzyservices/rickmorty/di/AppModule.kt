package com.mezzyservices.rickmorty.di

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mezzyservices.rickmorty.data.local.AppDatabase
import com.mezzyservices.rickmorty.data.local.CharacterDao
import com.mezzyservices.rickmorty.data.local.EpisodeDao
import com.mezzyservices.rickmorty.data.remote.RickMortyApi
import com.mezzyservices.rickmorty.data.repository.CharacterRepository
import com.mezzyservices.rickmorty.data.repository.EpisodeRepository
import com.mezzyservices.rickmorty.usecases.CheckEpisodeUseCase
import com.mezzyservices.rickmorty.usecases.GetCharacterUseCase
import com.mezzyservices.rickmorty.usecases.GetEpisodeListUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson) : Retrofit {

        val client = OkHttpClient.Builder().build()
        return Retrofit.Builder()
            .baseUrl("https://rickandmortyapi.com/api/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
    }

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun provideService(retrofit: Retrofit): RickMortyApi = retrofit.create(RickMortyApi::class.java)

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) = Room.databaseBuilder(
        appContext, AppDatabase::class.java, "RM_DATABASE"
    ).fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    fun provideCharacterDao(db: AppDatabase) = db.characterDao()

    @Singleton
    @Provides
    fun provideEpisodeDao(db: AppDatabase) = db.episodeDao()

    @Provides
    fun provideCharacterRepository(rickMortyApi: RickMortyApi, characterDao: CharacterDao) =
        CharacterRepository(rickMortyApi, characterDao)

    @Singleton
    @Provides
    fun provideEpisodeRepository(rickMortyApi: RickMortyApi, episodeDao: EpisodeDao) =
        EpisodeRepository(episodeDao, rickMortyApi)

    @Singleton
    @Provides
    fun provideGetCharacterUseCase(characterRepository: CharacterRepository) =
        GetCharacterUseCase(characterRepository)

    @Singleton
    @Provides
    fun provideGetEpisodeListUseCase(episodeRepository: EpisodeRepository) =
        GetEpisodeListUseCase(episodeRepository)

    @Singleton
    @Provides
    fun provideCheckEpisodeUseCase(episodeRepository: EpisodeRepository) =
        CheckEpisodeUseCase(episodeRepository)

}