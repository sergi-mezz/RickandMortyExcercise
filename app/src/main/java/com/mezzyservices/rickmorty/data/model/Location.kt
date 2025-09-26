package com.mezzyservices.rickmorty.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Location(
    val name: String?,
    @PrimaryKey val url: String
)