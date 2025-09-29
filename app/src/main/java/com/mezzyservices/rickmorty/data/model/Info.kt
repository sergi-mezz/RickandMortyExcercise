package com.mezzyservices.rickmorty.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Info (

    @PrimaryKey val count: Int?,
    val pages: Int?,
    val next: String?,
    val prev: String?
)