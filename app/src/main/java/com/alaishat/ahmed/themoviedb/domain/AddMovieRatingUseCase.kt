package com.alaishat.ahmed.themoviedb.domain

import com.alaishat.ahmed.themoviedb.domain.repository.MovieListRepository
import javax.inject.Inject

/**
 * Created by Ahmed Al-Aishat on Sep/09/2023.
 * The Movie DB Project.
 */
class AddMovieRatingUseCase @Inject constructor(
    private val movieListRepository: MovieListRepository,
) {
    suspend operator fun invoke(movieId: Int, rating: Int) =
        movieListRepository.addMovieRating(movieId = movieId, rating = rating)
}