package com.alaishat.ahmed.themoviedb.datasource.movie.source.remote

import androidx.paging.PagingData
import com.alaishat.ahmed.themoviedb.data.model.CreditDataModel
import com.alaishat.ahmed.themoviedb.data.model.GenreDataModel
import com.alaishat.ahmed.themoviedb.data.model.MovieAccountStatusDataModel
import com.alaishat.ahmed.themoviedb.data.model.MovieDataModel
import com.alaishat.ahmed.themoviedb.data.model.MovieDetailsDataModel
import com.alaishat.ahmed.themoviedb.data.model.MovieListTypeDataModel
import com.alaishat.ahmed.themoviedb.data.model.ReviewDataModel
import com.alaishat.ahmed.themoviedb.data.source.remote.RemoteMoviesDataSource
import com.alaishat.ahmed.themoviedb.data.source.remote.paging.CacheablePagingSource
import com.alaishat.ahmed.themoviedb.data.source.remote.paging.defaultPagerOf
import com.alaishat.ahmed.themoviedb.datasource.constants.ACCOUNT_ID
import com.alaishat.ahmed.themoviedb.datasource.movie.model.MovieCreditsRes
import com.alaishat.ahmed.themoviedb.datasource.movie.model.MovieGenreListRes
import com.alaishat.ahmed.themoviedb.datasource.movie.model.MovieListRes
import com.alaishat.ahmed.themoviedb.datasource.movie.model.MovieRatingReq
import com.alaishat.ahmed.themoviedb.datasource.movie.model.MovieReviewsRes
import com.alaishat.ahmed.themoviedb.datasource.movie.model.NetworkMovieAccountStatus
import com.alaishat.ahmed.themoviedb.datasource.movie.model.NetworkMovieDetails
import com.alaishat.ahmed.themoviedb.datasource.movie.model.ToggleWatchlistMovieReq
import com.alaishat.ahmed.themoviedb.datasource.movie.model.mapToCreditsDataModels
import com.alaishat.ahmed.themoviedb.datasource.movie.model.mapToGenresDataModels
import com.alaishat.ahmed.themoviedb.datasource.movie.model.mapToMoviesDataModel
import com.alaishat.ahmed.themoviedb.datasource.movie.model.mapToReviewsDataModels
import com.alaishat.ahmed.themoviedb.datasource.movie.model.toMovieAccountStatusDataModel
import com.alaishat.ahmed.themoviedb.datasource.movie.model.toMoviesDetailsDataModel
import com.alaishat.ahmed.themoviedb.datasource.remote.KtorClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.coroutines.flow.Flow

/**
 * Created by Ahmed Al-Aishat on Jun/25/2023.
 * The Movie DB Project.
 */
class KtorMoviesDataSource(
    private val ktorClient: KtorClient,
) : RemoteMoviesDataSource {

    override suspend fun getMoviesPage(
        movieListTypeDataModel: MovieListTypeDataModel,
        page: Int
    ): List<MovieDataModel> {
        val res: MovieListRes = ktorClient.call {
            get("movie/${movieListTypeDataModel.listApiPath}?page=$page")
//            &without_keywords=158718
        }
        return res.results.mapToMoviesDataModel()
    }

    override fun getCacheableMoviesPagingFlow(
        movieListTypeDataModel: MovieListTypeDataModel,
        pageCachingHandler: suspend (page: Int, pageData: List<MovieDataModel>) -> Unit
    ): Flow<PagingData<MovieDataModel>> {
        val pager = defaultPagerOf(
            pagingSourceFactory = {
                CacheablePagingSource(
                    pageDataProvider = { page ->
                        getMoviesPage(movieListTypeDataModel = movieListTypeDataModel, page = page)
                    },
                    pageCachingHandler = pageCachingHandler,
                )
            })
        return pager.flow
    }

    override suspend fun searchMovie(query: String, page: Int): List<MovieDataModel> {
        val res: MovieListRes = ktorClient.call {
            get("search/movie?query=$query&page=$page")
        }
        return res.results.mapToMoviesDataModel()
    }

    override fun getSearchMoviePagingFlow(
        query: String,
        pageCachingHandler: suspend (page: Int, pageData: List<MovieDataModel>) -> Unit,
    ): Flow<PagingData<MovieDataModel>> {
        val pager = defaultPagerOf(
            pagingSourceFactory = {
                CacheablePagingSource(
                    pageDataProvider = { page ->
                        searchMovie(query, page)
                    },
                    pageCachingHandler = pageCachingHandler,
                )
            })
        return pager.flow
    }

    override suspend fun getMovieDetails(movieId: Int): MovieDetailsDataModel {
        val res: NetworkMovieDetails = ktorClient.call {
            get("movie/$movieId")
        }
        return res.toMoviesDetailsDataModel()
    }

    override suspend fun getMovieCredits(movieId: Int): List<CreditDataModel> {
        val res: MovieCreditsRes = ktorClient.call {
            get("movie/$movieId/credits")
        }
        return res.cast.mapToCreditsDataModels()
    }

    override suspend fun getMovieReviews(movieId: Int, page: Int): List<ReviewDataModel> {
        val res: MovieReviewsRes = ktorClient.call {
            get("movie/$movieId/reviews?page=$page")
        }
        return res.reviews.mapToReviewsDataModels()
    }

    override fun getMovieReviewsPagingFlow(
        movieId: Int,
        pageCachingHandler: suspend (page: Int, pageData: List<ReviewDataModel>) -> Unit
    ): Flow<PagingData<ReviewDataModel>> {
        return defaultPagerOf(
            pagingSourceFactory = {
                CacheablePagingSource(
                    pageDataProvider = { page ->
                        getMovieReviews(movieId = movieId, page = page)
                    },
                    pageCachingHandler = pageCachingHandler
                )
            }
        ).flow
    }

    override suspend fun addMovieRating(movieId: Int, rating: Int) {
        ktorClient.call<Unit> {
            post("movie/$movieId/rating") {
                setBody(body = MovieRatingReq(value = rating))
            }
        }
    }

    override suspend fun getMovieAccountStatus(movieId: Int): MovieAccountStatusDataModel {
        val res: NetworkMovieAccountStatus = ktorClient.call {
            get("movie/$movieId/account_states")
        }
        return res.toMovieAccountStatusDataModel()
    }

    override suspend fun getMovieGenreList(): List<GenreDataModel> {
        val res: MovieGenreListRes = ktorClient.call {
            get("genre/movie/list")
        }
        return res.genres.mapToGenresDataModels()
    }

    override suspend fun getWatchlist(page: Int): List<MovieDataModel> {
        val res: MovieListRes = ktorClient.call {
            get("account/$ACCOUNT_ID/watchlist/movies?page=$page")
        }
        return res.results.mapToMoviesDataModel()
    }

    override fun getWatchlistPagingFlow(
        pageCachingHandler: suspend (page: Int, pageData: List<MovieDataModel>) -> Unit,
    ): Flow<PagingData<MovieDataModel>> {
        val pager = defaultPagerOf(
            pagingSourceFactory = {
                CacheablePagingSource(
                    pageDataProvider = { page ->
                        getWatchlist(page)
                    },
                    pageCachingHandler = pageCachingHandler
                )
            }
        )
        return pager.flow
    }

    override suspend fun toggleWatchlistMovie(movieId: Int, watchlist: Boolean) {
        ktorClient.call<Unit> {
            post("account/$ACCOUNT_ID/watchlist") {
                setBody(
                    body = ToggleWatchlistMovieReq(
                        mediaId = movieId,
                        watchlist = watchlist,
                    )
                )
            }
        }
    }
}