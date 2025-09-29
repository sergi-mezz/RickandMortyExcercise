package com.mezzyservices.rickmorty.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.mezzyservices.rickmorty.data.model.Episode
@Composable
fun CharacterDetailScreen(characterId: Int?, onWatchMapClicked: () -> Unit) {

    val viewModel = hiltViewModel<CharacterDetailViewModel>()
    Content(viewModel) { onWatchMapClicked() }
    viewModel.getCharacter(characterId!!)
}


@Composable
fun Content(viewModel: CharacterDetailViewModel, onWatchMapClicked: () -> Unit) {

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
            CharacterCard(viewModel) { onWatchMapClicked() }
        }

        null -> {}
    }
}


@Composable
fun CharacterCard(viewModel: CharacterDetailViewModel, onWatchMapClicked: () -> Unit) {

    val favourite by viewModel.isFavourite.observeAsState()

    Card(modifier = Modifier.padding(top = 35.dp, start = 5.dp, end = 5.dp, bottom = 35.dp)) {
        Column(modifier = Modifier.fillMaxWidth().padding(2.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    AsyncImage(
                        modifier = Modifier.size(125.dp),
                        model = viewModel.character.image,
                        contentDescription = ""
                    )
                    IconButton(
                        onClick = { viewModel.checkFavourite() }
                    ) {
                        Icon(
                            imageVector = if(favourite == true) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            tint = if(favourite == true) Color.Red else Color.LightGray,
                            contentDescription = ""
                        )
                    }
                }


                Column(modifier = Modifier.padding(start = 5.dp)) {
                    Text(viewModel.character.gender ?: "")
                    Text(viewModel.character.species ?: "")
                    Text(viewModel.character.status ?: "")
                    Text(viewModel.character.location?.name ?: "")
                    Button(
                        onClick = { onWatchMapClicked() },
                        shape = RectangleShape,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray, contentColor = Color.White)
                    ) {
                        Text("Ver en\nmapa")
                    }
                }
            }

            LazyColumn {
                item {
                    Text(text = "Capitulos:", modifier = Modifier.padding(5.dp), fontWeight = FontWeight.Bold)
                }
                items(
                    viewModel.episodeList
                ) { episode: Episode ->
                    EpisodeCard(episode) { viewModel.addWatchedEpisode(episode) }
                }
            }
        }
    }
}

@Composable
fun EpisodeCard(episode: Episode, onClickCheck: () -> Unit) {

    Card(border = BorderStroke(1.dp, Color.LightGray), modifier = Modifier.fillMaxWidth().padding(top = 4.dp)) {

        var watched by rememberSaveable { mutableStateOf( episode.alreadyWatched ) }

        Row(modifier = Modifier.fillMaxWidth().padding(2.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "Episodio " + episode.id.toString() + " "  + episode.name!!, modifier = Modifier.width(200.dp))
            IconButton(onClick = {
                onClickCheck()
                watched = !watched
            },
                modifier = Modifier.padding(end = 20.dp)) {
                Icon(
                    imageVector = if(watched) Icons.Filled.CheckCircle else Icons.Outlined.CheckCircle,
                    tint = if(watched) Color.Green else Color.LightGray,
                    contentDescription = "",
                )
            }
        }
    }

}

