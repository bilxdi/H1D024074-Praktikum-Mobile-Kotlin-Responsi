package com.example.pokemon.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.pokemon.data.model.Pokemon
import com.example.pokemon.ui.viewmodel.PokemonUiState
import com.example.pokemon.ui.viewmodel.PokemonViewModel
import com.example.pokemon.util.PokemonConstants.IMG_URL

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DaftarPokemonScreen(navController: NavController? = null, viewModel: PokemonViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    var searchQuery by rememberSaveable { mutableStateOf("") }

    when (val state = uiState) {
        is PokemonUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is PokemonUiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Error: ${state.message}", color = MaterialTheme.colorScheme.error)
            }
        }
        is PokemonUiState.Success -> {
            val filteredPokemon = if (searchQuery.isBlank()) {
                state.pokemon
            } else {
                state.pokemon.filter {
                    it.name.contains(searchQuery, ignoreCase = true)
                }
            }
            StatelessDaftarPokemon(
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it },
                pokemon = filteredPokemon,
                onPokemonClick = { pokemon ->
                    navController?.navigate("detail/${pokemon.id}")
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatelessDaftarPokemon(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    pokemon: List<Pokemon>,
    onPokemonClick: (Pokemon) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Daftar Pokemon") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                label = { Text("Cari Pokemon...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                singleLine = true
            )

            Text(
                text = "Daftar Pokemon",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            if (pokemon.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Pokemon tidak ditemukan.")
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(pokemon) { item ->
                        PokemonItemCard(pokemon = item) {
                            onPokemonClick(item)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PokemonItemCard(pokemon: Pokemon, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            val imageModel: Any = "$IMG_URL${pokemon.id}.png"
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                AsyncImage(
                    model = imageModel,
                    contentDescription = pokemon.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = pokemon.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = pokemon.types,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}
