package com.alaishat.ahmed.themoviedb.domain.usecase

import androidx.paging.PagingData
import com.alaishat.ahmed.themoviedb.domain.feature.movie.model.ReviewDomainModel
import com.alaishat.ahmed.themoviedb.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow

/**
 * Created by Ahmed Al-Aishat on Sep/09/2023.
 * The Movie DB Project.
 */
class GetMovieReviewsPagingFlowUseCase(
    private val moviesRepository: MoviesRepository,
) {
    operator fun invoke(movieId: Int): Flow<PagingData<ReviewDomainModel>> =
        moviesRepository.getMovieReviewsPagingFlow(movieId = movieId)
}