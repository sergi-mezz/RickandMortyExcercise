package com.mezzyservices.rickmorty.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.mezzyservices.rickmorty.data.local.FavouriteCharacter
import kotlin.math.roundToInt


@Composable
fun FavouriteCharactersScreen(onCharacterCardClicked: (Int) -> Unit) {

    val viewModel = hiltViewModel<FavouriteEpisodesViewModel>()
    Content(viewModel, onCharacterCardClicked)
    viewModel.getEpisodes()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Content(viewModel: FavouriteEpisodesViewModel, onCharacterCardClicked: (Int) -> Unit) {

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
                        CharacterCard(it, viewModel, onCharacterCardClicked)
                    }

                }
            }
        }

        null -> {}
    }

}

@Composable
fun CharacterCard(character: FavouriteCharacter, viewModel: FavouriteEpisodesViewModel,onCharacterCardClicked: (Int) -> Unit) {

    var visible by remember { mutableStateOf(true) }
    val anchoredDraggableState = remember {
        AnchoredDraggableState(0, DraggableAnchors { 0 at 0f; 1 at -250f })
    }
    AnimatedVisibility(visible) {
        Card(
            modifier = Modifier
                .padding(5.dp)
                .height(100.dp)
                .clickable {
                    onCharacterCardClicked(character.id)
                }
        ) {

            Box(contentAlignment = Alignment.CenterEnd) {

                IconButton(
                    modifier = Modifier.padding(end = 20.dp),
                    onClick = {
                        viewModel.deleteFavourite(character)
                        visible = false
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        tint = Color.Red,
                        contentDescription = ""
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset {
                            IntOffset(anchoredDraggableState.offset.roundToInt(), 0)
                        }.anchoredDraggable(
                            state = anchoredDraggableState,
                            orientation = Orientation.Horizontal,
                        )
                ) {
                    AsyncImage(model = character.image, contentDescription = "", modifier = Modifier.fillMaxSize().weight(1f))
                    Text(character.name, modifier = Modifier.fillMaxSize().weight(3f).background(Color.LightGray))
                }
            }
        }
    }
}