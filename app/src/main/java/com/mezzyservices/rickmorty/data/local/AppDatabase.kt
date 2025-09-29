package com.mezzyservices.rickmorty.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mezzyservices.rickmorty.data.model.Character
import com.mezzyservices.rickmorty.data.model.Episode
import com.mezzyservices.rickmorty.data.model.Info
import com.mezzyservices.rickmorty.data.model.Location


@Database(entities = [Character::class, Episode::class, Location::class, FavouriteCharacter::class, Info::class], version = 6)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
    abstract fun episodeDao(): EpisodeDao

    abstract fun favouriteDao(): FavouriteDao

    abstract fun infoDao(): InfoDao


}