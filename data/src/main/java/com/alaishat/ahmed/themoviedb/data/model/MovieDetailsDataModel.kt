package com.alaishat.ahmed.themoviedb.data.model

/**
 * Created by Ahmed Al-Aishat on Sep/09/2023.
 * The Movie DB Project.
 */
data class MovieDetailsDataModel(
    val backdropPath: String?,
    val genreDataModels: List<GenreDataModel>,
    val id: Int,
    val overview: String,
    val posterPath: String?,
    val releaseDate: String,//AHMED_TODO: convert me to date
    val runtime: Int,
    val title: String,
    val voteAverage: Float,
)