package com.alaishat.ahmed.themoviedb.data.source.remote.paging

import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import kotlinx.coroutines.flow.flowOf

/**
 * Created by Ahmed Al-Aishat on Sep/15/2023.
 * The Movie DB Project.
 */
private const val DEFAULT_PAGE_SIZE = 20

private val defaultPagingConfig = PagingConfig(pageSize = DEFAULT_PAGE_SIZE)

fun <Value : Any> defaultPagerOf(
    pagingSourceFactory: () -> PagingSource<Int, Value>,
) = Pager(
    config = defaultPagingConfig,
    pagingSourceFactory = pagingSourceFactory
)

fun <Value : Any> pagerFlowOf(
    data: List<Value>
) = flowOf(pagerOf(data = data))

private fun <Value : Any> pagerOf(
    data: List<Value>
) = PagingData.from(
    data = data,
    sourceLoadStates = LoadStates(
        refresh = LoadState.NotLoading(endOfPaginationReached = true),
        prepend = LoadState.NotLoading(endOfPaginationReached = true),
        append = LoadState.NotLoading(endOfPaginationReached = true)
    )
)