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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Content(viewModel: FavouriteEpisodesViewModel) {

    val command by viewModel.command.observeAsState()

    when (command) {
        is FavouriteEpisodesViewModel.Command.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is FavouriteEpisodesViewModel.Command.Error -> {
            Text("Error")
        }

        is FavouriteEpisodesViewModel.Command.Success -> {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Favoritos") }
                    )
                }
            ) { innerPadding ->

                LazyColumn(modifier = Modifier.padding(innerPadding)) {

                    items(viewModel.favouriteCharacterList) {
                        CharacterCard(it)
                    }

                }
            }
        }

        null -> {}
    }

}

@Composable
fun CharacterCard(character: FavouriteCharacter) {

    Card(modifier = Modifier.padding(5.dp)) {

        Row(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(model = character.image, contentDescription = "")
            Text(character.name)
        }
    }

}