package com.alaishat.ahmed.themoviedb.domain.usecase

import androidx.paging.PagingData
import androidx.paging.filter
import com.alaishat.ahmed.themoviedb.domain.common.model.MovieDomainModel
import com.alaishat.ahmed.themoviedb.domain.constants.DEBOUNCE_TIMEOUT
import com.alaishat.ahmed.themoviedb.domain.repository.MoviesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest


class GetWatchListUseCase(
    private val moviesRepository: MoviesRepository,
) {
    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    operator fun invoke(queryFlow: Flow<String>): Flow<PagingData<MovieDomainModel>> {
        val pagingFlow = moviesRepository.getWatchListPagingFlow()

        return queryFlow
            .debounce(DEBOUNCE_TIMEOUT)
            .flatMapLatest { query ->
                pagingFlow.mapLatest { list ->
                    list.filter { movie ->
                        movie.title.contains(query, ignoreCase = true) ||
                                movie.overview.contains(query, ignoreCase = true)
                    }
                }
            }
    }
}