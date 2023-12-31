package com.alaishat.ahmed.themoviedb.data.source.remote

import androidx.paging.PagingData
import com.alaishat.ahmed.themoviedb.data.model.CreditDataModel
import com.alaishat.ahmed.themoviedb.data.model.GenreDataModel
import com.alaishat.ahmed.themoviedb.data.model.MovieAccountStatusDataModel
import com.alaishat.ahmed.themoviedb.data.model.MovieDataModel
import com.alaishat.ahmed.themoviedb.data.model.MovieDetailsDataModel
import com.alaishat.ahmed.themoviedb.data.model.MovieListTypeDataModel
import com.alaishat.ahmed.themoviedb.data.model.ReviewDataModel
import kotlinx.coroutines.flow.Flow

/**
 * Created by Ahmed Al-Aishat on Jun/25/2023.
 * The Movie DB Project.
 */
interface RemoteMoviesDataSource {
    suspend fun getMoviesPage(
        movieListTypeDataModel: MovieListTypeDataModel,
        page: Int
    ): List<MovieDataModel>

    fun getCacheableMoviesPagingFlow(
        movieListTypeDataModel: MovieListTypeDataModel,
        pageCachingHandler: suspend (page: Int, pageData: List<MovieDataModel>) -> Unit,
    ): Flow<PagingData<MovieDataModel>>

    suspend fun searchMovie(query: String, page: Int): List<MovieDataModel>
    fun getSearchMoviePagingFlow(
        query: String,
        pageCachingHandler: suspend (page: Int, pageData: List<MovieDataModel>) -> Unit,
    ): Flow<PagingData<MovieDataModel>>

    suspend fun getMovieDetails(movieId: Int): MovieDetailsDataModel
    suspend fun getMovieCredits(movieId: Int): List<CreditDataModel>
    suspend fun getMovieReviews(movieId: Int, page: Int): List<ReviewDataModel>
    fun getMovieReviewsPagingFlow(
        movieId: Int,
        pageCachingHandler: suspend (page: Int, pageData: List<ReviewDataModel>) -> Unit
    ): Flow<PagingData<ReviewDataModel>>

    suspend fun addMovieRating(movieId: Int, rating: Int)
    suspend fun getMovieAccountStatus(movieId: Int): MovieAccountStatusDataModel
    suspend fun getMovieGenreList(): List<GenreDataModel>

    suspend fun getWatchlist(page: Int): List<MovieDataModel>
    fun getWatchlistPagingFlow(
        pageCachingHandler: suspend (page: Int, pageData: List<MovieDataModel>) -> Unit,
    ): Flow<PagingData<MovieDataModel>>
    suspend fun toggleWatchlistMovie(movieId: Int, watchlist: Boolean)
}