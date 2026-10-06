package com.example.pokemon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pokemon.ui.screen.DaftarPokemonScreen
import com.example.pokemon.ui.screen.DetailPokemonScreen
import com.example.pokemon.ui.theme.PokemonTheme
import com.example.pokemon.ui.viewmodel.PokemonViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PokemonTheme {
                val navController = rememberNavController()
                val pokemonViewModel: PokemonViewModel = viewModel()
                NavHost(navController = navController, startDestination = "daftar_pokemon") {
                    composable("daftar_pokemon") {
                        DaftarPokemonScreen(
                            navController = navController,
                            viewModel = pokemonViewModel
                        )
                    }
                    composable(
                        route = "detail/{pokemonId}",
                        arguments = listOf(
                            navArgument("pokemonId") {
                                type = NavType.IntType
                            }
                        )
                    ) { backStackEntry ->
                        val pokemonId = backStackEntry.arguments?.getInt("pokemonId") ?: 0
                        DetailPokemonScreen(
                            pokemonId = pokemonId,
                            navController = navController,
                            viewModel = pokemonViewModel
                        )
                    }
                }
            }
        }
    }
}
