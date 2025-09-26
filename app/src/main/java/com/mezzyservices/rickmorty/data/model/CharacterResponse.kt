package com.mezzyservices.rickmorty.data.model

data class CharacterResponse (
    val info: Info,
    val results: List<Character>
)