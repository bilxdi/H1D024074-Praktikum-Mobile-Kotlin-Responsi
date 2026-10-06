package com.example.pokemon.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemon.data.model.Pokemon
import com.example.pokemon.network.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface PokemonUiState {
    object Loading : PokemonUiState
    data class Success(val products: List<Pokemon>) : PokemonUiState
    data class Error(val message: String) : PokemonUiState
}

class PokemonViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<PokemonUiState>(PokemonUiState.Loading)

    val uiState: StateFlow<PokemonUiState> = _uiState.asStateFlow()

    init {
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch {
            _uiState.value = PokemonUiState.Loading
            try {
                val pokemonResponse = ApiClient.instance.getProducts()

                _uiState.value = PokemonUiState.Success(
                    products = pokemonResponse
                )

            } catch (e: Exception) {
                _uiState.value = PokemonUiState.Error(
                    message = "Gagal memuat data: ${e.localizedMessage}"
                )
            }
        }
    }
}