package com.example.pokemon.data.model

data class Pokemon(
    val id: Int,
    val category_id: Int,
    val name: String,
    val description: String?,
    val price: Double,
    val stock: Int,
    val img: String
)