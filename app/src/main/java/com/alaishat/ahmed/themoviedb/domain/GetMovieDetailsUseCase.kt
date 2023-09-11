package com.alaishat.ahmed.themoviedb.domain

import com.alaishat.ahmed.themoviedb.domain.model.MovieDetails
import com.alaishat.ahmed.themoviedb.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

/**
 * Created by Ahmed Al-Aishat on Sep/09/2023.
 * The Movie DB Project.
 */
class GetMovieDetailsUseCase @Inject constructor(
    private val moviesRepository: MoviesRepository,
) {
    operator fun invoke(movieId: Int): Flow<MovieDetails> = combine(
        moviesRepository.getMovieDetails(movieId = movieId),
        moviesRepository.observeWatchlist()
    ) { details, moviesStatus ->
        details.copy(watchlist = moviesStatus.contains(details.id))
    }
}