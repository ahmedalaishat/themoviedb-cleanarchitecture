package com.alaishat.ahmed.themoviedb.data.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.alaishat.ahmed.themoviedb.data.model.mapToMovies
import com.alaishat.ahmed.themoviedb.data.source.network.NetworkMoviesDataSource
import com.alaishat.ahmed.themoviedb.domain.model.Movie

/**
 * Created by Ahmed Al-Aishat on Jun/26/2023.
 * The Movie DB Project.
 */
class MoviesPagingSource(
    private val moviesDataSource: NetworkMoviesDataSource,
    private val movieListPath: String,
) : PagingSource<Int, Movie>() {
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val page = params.key ?: 1
            // calling the paging api
            val fetchedMovies = moviesDataSource.getMoviesPage(movieListPath, page).mapToMovies()

            LoadResult.Page(
                data = fetchedMovies,
                prevKey = if (page > 1) page.minus(1) else null,
                nextKey = if (fetchedMovies.isNotEmpty()) page.plus(1) else null,
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}