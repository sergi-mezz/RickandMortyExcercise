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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.sharp.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
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
    var filterQuery by remember { mutableStateOf("") }
    val filteredItems = remember(characters.itemSnapshotList, filterQuery) {
        if (filterQuery.isEmpty()) {
            characters.itemSnapshotList.items
        } else {
            characters.itemSnapshotList.items.filter { item ->
                item.name!!.contains(filterQuery, ignoreCase = true)
            }
        }
    }

    PullToRefreshBox(
        modifier = Modifier.fillMaxSize(),
        onRefresh = { characters.refresh() },
        isRefreshing = characters.loadState.refresh is LoadState.Loading
    ) {

        val scope = rememberCoroutineScope()
        val snackBarHostState = remember { SnackbarHostState() }

        Scaffold(
            topBar = { TopAppBar(modifier = Modifier.padding(top = 25.dp), filterQuery, onFavouritesClicked, { filterQuery = it } ) },
            snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
        ) { innerPadding ->

            when (characters.loadState.refresh) {
                is LoadState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is LoadState.Error -> {
                    scope.launch {
                        snackBarHostState.showSnackbar(characters.loadState.refresh.toString())
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items(filteredItems.count()) { index ->
                            EpisodeCard(filteredItems[index], onCardClicked = {
                                onCardClicked(filteredItems[index].id!!)
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
    filterQuery: String,
    onFavouritesClicked: () -> Unit,
    onQueryChanged: (String) -> Unit
) {

    Column(modifier = modifier) {
        SearchBar(
            inputField = {
                InputField(
                    query = filterQuery,
                    onQueryChange = { onQueryChanged(it) },
                    onSearch = {

                    },
                    expanded = false,
                    onExpandedChange = { },
                    placeholder = {Text("Busqueda")},
                    trailingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "")}
                )
            },
            expanded = false,
            onExpandedChange = {},
        ) {

        }

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Text("TODO Filtros")
            IconButton(onClick = {
                onFavouritesClicked()
            }) {
                Icon(imageVector = Icons.Sharp.Favorite, tint = Color.Red, contentDescription = "")
            }
        }
    }

}

@Composable
fun EpisodeCard(character: Character, onCardClicked: () -> Unit) {
    Card(modifier = Modifier
        .padding(5.dp)
        .clickable(onClick = { onCardClicked() })) {
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