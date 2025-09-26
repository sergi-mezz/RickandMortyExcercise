package com.mezzyservices.rickmorty.data.local

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mezzyservices.rickmorty.data.model.Character
import com.mezzyservices.rickmorty.data.model.Info
import com.mezzyservices.rickmorty.data.model.Location

class Converters {

    @TypeConverter
    fun locationToString(location: Location) = "${location.name}*${location.url}"

    @TypeConverter
    fun stringToLocation(location: String) = Location(location.substringBefore("*"), location.substringAfter("*"))

    @TypeConverter
    fun listToString(array: List<String>) = array.joinToString(",")

    @TypeConverter
    fun stringToList(str: String) = str.split(",").map { it.trim() }

}