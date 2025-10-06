package com.mezzyservices.rickmorty.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.sharp.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults.InputField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil3.compose.AsyncImage
import com.mezzyservices.rickmorty.data.model.Character
import kotlinx.coroutines.launch


@Composable
fun CharacterListScreen(
    onCardClicked: (Int) -> Unit,
    onFavouritesClicked: () -> Unit
) {
    val viewModel = hiltViewModel<CharacterListViewModel>()
    val characters = viewModel.characterListFlow.collectAsLazyPagingItems()
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    var query by rememberSaveable { mutableStateOf("") }

    var statusFilterList by remember { mutableStateOf(setOf("Ver todos")) }
    var specieFilterList by remember { mutableStateOf(setOf("Ver todas")) }
    var statusFilterSelection by remember { mutableStateOf("") }
    var specieFilterSelection by remember { mutableStateOf("") }

    val listState = rememberSaveable(saver = LazyListState.Saver) {
        LazyListState()
    }

    PullToRefreshBox(
        isRefreshing = characters.loadState.refresh is LoadState.Loading,
        onRefresh = { characters.refresh() }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    modifier = Modifier.padding(top = 5.dp),
                    query,
                    onFavouritesClicked,
                    { query = it },
                    statusFilterList,
                    specieFilterList,
                    { specieFilterSelection = if (it == "Ver todas") "" else it },
                    { statusFilterSelection = if (it == "Ver todos") "" else it }
                )
            },
            snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
        ) { innerPadding ->

            when (characters.loadState.refresh) {
                is LoadState.Loading -> {}

                is LoadState.Error -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {}
                    LaunchedEffect("refresh_error") {
                        scope.launch {
                            snackBarHostState.showSnackbar(
                                (characters.loadState.refresh as LoadState.Error).error.message
                                    ?: "Error"
                            )
                        }
                    }
                }

                is LoadState.NotLoading -> {

                    val tempStatusSet = mutableSetOf<String>()
                    val tempSpecieSet = mutableSetOf<String>()
                    characters.itemSnapshotList.items.forEach {
                        it.status?.let { tempStatusSet.add(it) }
                        it.species?.let { tempSpecieSet.add(it) }
                    }

                    statusFilterList = statusFilterList + tempStatusSet
                    specieFilterList = specieFilterList + tempSpecieSet

                    LazyColumn(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        state = listState
                    ) {
                        if (query.isEmpty() && statusFilterSelection.isEmpty() && specieFilterSelection.isEmpty()) {
                            items(characters.itemCount, characters.itemKey { it.id!! }) { index ->
                                if (characters[index] != null)
                                    CharacterCard(characters[index]!!, onCardClicked = {
                                        onCardClicked(characters[index]!!.id!!)
                                    })
                            }
                        } else
                            items(characters.itemSnapshotList.items.filter {
                                it.name!!.contains(query, true) && it.species!!.contains(
                                    specieFilterSelection
                                ) && it.status!!.contains(statusFilterSelection)
                            }) { it ->
                                CharacterCard(it, onCardClicked = {
                                    onCardClicked(it.id!!)
                                })
                            }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    modifier: Modifier,
    query: String,
    onFavouritesClicked: () -> Unit,
    onQueryChanged: (String) -> Unit,
    statusFilterList: Set<String>,
    specieFilterList: Set<String>,
    onSpeciesSelected: (String) -> Unit,
    onStatusSelected: (String) -> Unit
) {

    Column(modifier = modifier) {
        SearchBar(
            inputField = {
                InputField(
                    query = query,
                    onQueryChange = { onQueryChanged(it) },
                    onSearch = {

                    },
                    expanded = false,
                    onExpandedChange = { },
                    placeholder = { Text("Busqueda") },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = ""
                        )
                    }
                )
            },
            expanded = false,
            onExpandedChange = {},
        ) {

        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Filtrar por:")
                Box() {
                    var expanded by remember { mutableStateOf(false) }
                    Button(
                        onClick = { expanded = !expanded }
                    ) {
                        Text("Estatus")
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            statusFilterList.forEach {
                                DropdownMenuItem(
                                    text = { Text(it) },
                                    onClick = {
                                        expanded = false
                                        onStatusSelected(it)
                                    }
                                )
                            }
                        }
                    }
                }
                Box() {
                    var expanded by remember { mutableStateOf(false) }
                    Button(
                        onClick = { expanded = !expanded }
                    ) {
                        Text("Especie")
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {

                            specieFilterList.forEach {
                                DropdownMenuItem(
                                    text = { Text(it) },
                                    onClick = {
                                        expanded = false
                                        onSpeciesSelected(it)
                                    }
                                )
                            }
                        }
                    }
                }

            }
            IconButton(onClick = {
                onFavouritesClicked()
            }) {
                Icon(imageVector = Icons.Sharp.Favorite, tint = Color.Red, contentDescription = "")
            }
        }
    }

}

@Composable
fun CharacterCard(character: Character, onCardClicked: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(5.dp)
            .clickable(onClick = { onCardClicked() })
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                modifier = Modifier.size(100.dp),
                model = character.image,
                contentDescription = ""
            )

            Column(modifier = Modifier.padding(start = 5.dp)) {
                Text(character.name ?: "")
                Text(character.species ?: "")
                Text(character.status ?: "")
            }
        }
    }
}