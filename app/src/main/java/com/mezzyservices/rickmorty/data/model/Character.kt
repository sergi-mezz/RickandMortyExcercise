package com.mezzyservices.rickmorty.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Character(
    @PrimaryKey val id: Int?,
    val name: String? = null,
    val species: String? = null,
    val gender: String? = null,
    val status: String? = null,
    val image: String? = null,
    val location: Location? = null,
    val episode: List<String>?,
)
