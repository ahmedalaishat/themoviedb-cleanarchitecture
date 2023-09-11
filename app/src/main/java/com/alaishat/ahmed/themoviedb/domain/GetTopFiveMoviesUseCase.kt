package com.alaishat.ahmed.themoviedb.domain

import com.alaishat.ahmed.themoviedb.domain.model.Movie
import com.alaishat.ahmed.themoviedb.domain.model.MovieListType
import com.alaishat.ahmed.themoviedb.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Created by Ahmed Al-Aishat on Jun/25/2023.
 * The Movie DB Project.
 */
class GetTopFiveMoviesUseCase @Inject constructor(
    private val moviesRepository: MoviesRepository,
) {
    operator fun invoke(): Flow<List<Movie>> = flow {
        val topFiveMovies = moviesRepository.getMoviesPageByType(MovieListType.TOP_RATED, 1).take(5)
        emit(topFiveMovies)
    }
}