package com.mezzyservices.rickmorty.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Episode(
    @PrimaryKey val id: Int?,
    val name: String?,
    val url: String?,
    var isFavourite: Boolean = false,
    var alreadyWatched: Boolean = false,
    val characters: List<String>?,
)
