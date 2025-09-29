package com.mezzyservices.rickmorty.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavouriteCharacter (
    @PrimaryKey val id: Int,
    val name: String,
    val image: String
)