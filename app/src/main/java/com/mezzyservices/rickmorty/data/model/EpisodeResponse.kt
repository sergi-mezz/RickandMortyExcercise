package com.mezzyservices.rickmorty.data.model

data class EpisodeResponse(
    val info: Info,
    val results: List<Episode>
)