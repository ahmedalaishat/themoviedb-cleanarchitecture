package com.alaishat.ahmed.themoviedb.domain.usecase

import com.alaishat.ahmed.themoviedb.domain.model.MovieDomainModel
import com.alaishat.ahmed.themoviedb.domain.model.MovieListTypeDomainModel
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
    operator fun invoke(): Flow<List<MovieDomainModel>> = flow {
        val topFiveMovies = moviesRepository.getTopFiveMovies().take(5)
        emit(topFiveMovies)
    }
}