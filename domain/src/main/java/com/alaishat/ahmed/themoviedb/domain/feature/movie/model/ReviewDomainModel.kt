package com.alaishat.ahmed.themoviedb.domain.feature.movie.model

/**
 * Created by Ahmed Al-Aishat on Sep/09/2023.
 * The Movie DB Project.
 */
data class ReviewDomainModel(
    val id: String,
    val content: String,
    val authorName: String,
    val authorAvatarPath: String?,
    val rating: String?,
)