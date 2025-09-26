package com.mezzyservices.rickmorty.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.mezzyservices.rickmorty.data.model.Episode
@Composable
fun CharacterDetailScreen(characterId: Int?) {

    val viewModel = hiltViewModel<CharacterDetailViewModel>()
    Content(viewModel)
    viewModel.getCharacter(characterId!!)
}


@Composable
fun Content(viewModel: CharacterDetailViewModel) {

    val command by viewModel.command.observeAsState()

    when (command) {
        is CharacterDetailViewModel.Command.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is CharacterDetailViewModel.Command.Error -> {
            Text("Error")
        }

        is CharacterDetailViewModel.Command.Success -> {
            CharacterCard(viewModel)
        }

        null -> {}
    }
}


@Composable
fun CharacterCard(viewModel: CharacterDetailViewModel) {

    Card(modifier = Modifier.padding(top = 20.dp, start = 5.dp, end = 5.dp)) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    modifier = Modifier.size(100.dp),
                    model = viewModel.character.image,
                    contentDescription = ""
                )

                Column(modifier = Modifier.padding(start = 5.dp)) {
                    Text(viewModel.character.gender ?: "")
                    Text(viewModel.character.species ?: "")
                    Text(viewModel.character.status ?: "")
                    Text(viewModel.character.location?.name ?: "")
                }
            }

            Text(text = "Capitulos:", modifier = Modifier.padding(5.dp))

            LazyColumn(
                contentPadding = PaddingValues(5.dp)
            ) {
                items(
                    viewModel.episodeList
                ) { episode: Episode ->
                    EpisodeCard(episode, { viewModel.addWatchedEpisode(episode) } , { viewModel.addFavouriteEpisode(episode) } )
                }
            }
        }
    }
}

@Composable
fun EpisodeCard(episode: Episode, onClickCheck: () -> Unit, onClickFavourite: () -> Unit) {

    Card(border = BorderStroke(1.dp, Color.LightGray), modifier = Modifier.fillMaxWidth()) {

        var favourite by rememberSaveable { mutableStateOf( episode.isFavourite ) }
        var watched by rememberSaveable { mutableStateOf( episode.alreadyWatched ) }

        Column(modifier = Modifier.padding(2.dp)) {
            Text("Episodio " + episode.id.toString() + ".   "  + episode.name!!)
            Row {
                IconButton(onClick = {
                    onClickCheck()
                    watched = !watched
                }) {
                    Icon(
                        imageVector = if(watched) Icons.Filled.CheckCircle else Icons.Outlined.CheckCircle,
                        contentDescription = ""
                    )
                }
                IconButton(onClick = {
                    onClickFavourite()
                    favourite = !favourite
                }) {
                    Icon(
                        imageVector = if(favourite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = ""
                    )
                }
            }
        }
    }

}

