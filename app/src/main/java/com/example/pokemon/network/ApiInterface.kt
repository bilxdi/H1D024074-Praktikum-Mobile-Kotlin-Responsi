package com.example.pokemon.network

import com.example.pokemon.util.PokemonConstants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import com.google.gson.annotations.SerializedName
import retrofit2.http.Path
import retrofit2.http.Query

data class PokemonListResponse(val results: List<PokemonListItem>)
data class PokemonListItem(val name: String)

data class PokemonDetailResponse(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    @SerializedName("base_experience") val baseExperience: Int?,
    val types: List<TypeSlot>
)
data class TypeSlot(val type: TypeName)
data class TypeName(val name: String)

interface ApiInterface {
    @GET("pokemon")
    suspend fun getPokemonList(@Query("limit") limit: Int = 20): PokemonListResponse

    @GET("pokemon/{name}")
    suspend fun getPokemonDetail(@Path("name") name: String): PokemonDetailResponse
}

object ApiClient {
    val instance: ApiInterface by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)
    }
}