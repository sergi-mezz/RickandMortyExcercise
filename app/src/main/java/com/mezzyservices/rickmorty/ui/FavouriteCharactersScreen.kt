package com.mezzyservices.rickmorty.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.mezzyservices.rickmorty.data.local.FavouriteCharacter


@Composable
fun FavouriteCharactersScreen() {

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
            LazyColumn(modifier = Modifier.padding(top = 35.dp, start = 5.dp, end = 5.dp, bottom = 35.dp)) {

                item(1) {
                    Text(text = "Favoritos", fontWeight = FontWeight.Bold)
                }
                items(viewModel.favouriteCharacterList) {
                    EpisodeCard(it)
                }

            }
        }

        null -> {  }
    }

}

@Composable
fun EpisodeCard(character: FavouriteCharacter) {

    Card(border = BorderStroke(1.dp, Color.LightGray), modifier = Modifier.fillMaxWidth()) {

        Row(modifier = Modifier.padding(2.dp)) {
            AsyncImage(model = character.image, contentDescription = "")
            Text(character.name)
        }
    }

}