package com.mezzyservices.rickmorty.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.mezzyservices.rickmorty.data.model.Episode


@Composable
fun FavouriteEpisodesScreen() {

    val viewModel = hiltViewModel<FavouriteEpisodesViewModel>()
    Content(viewModel)
    viewModel.getEpisodes()
}

@Composable
fun Content(viewModel: FavouriteEpisodesViewModel) {

    val command by viewModel.command.observeAsState()

    when(command) {
        is FavouriteEpisodesViewModel.Command.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is FavouriteEpisodesViewModel.Command.Error -> {
            Text("Error")
        }

        is FavouriteEpisodesViewModel.Command.Success -> {
            LazyColumn(contentPadding = PaddingValues(5.dp)) {

                item(1) {
                    Text("Favoritos")
                }
                items(viewModel.favouriteEpisodeList) {
                    EpisodeCard(it)
                }

            }
        }

        null -> {  }
    }

}

@Composable
fun EpisodeCard(episode: Episode) {

    Card(border = BorderStroke(1.dp, Color.LightGray), modifier = Modifier.fillMaxWidth()) {

        var favourite by rememberSaveable { mutableStateOf( episode.isFavourite ) }
        var watched by rememberSaveable { mutableStateOf( episode.alreadyWatched ) }

        Column(modifier = Modifier.padding(2.dp)) {
            Text("Episodio " + episode.id.toString() + ".   "  + episode.name!!)
            Text("${episode.url}")
        }
    }

}