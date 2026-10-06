package com.example.pokemon.data.model

data class Pokemon(
    val id: Int,
    val name: String,
    val img: String,
    val types: String,
    val height: Int,
    val weight: Int,
    val experience: Int
)