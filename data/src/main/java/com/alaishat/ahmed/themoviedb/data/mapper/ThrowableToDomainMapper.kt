package com.alaishat.ahmed.themoviedb.data.mapper

import com.alaishat.ahmed.themoviedb.data.source.remote.exception.ApiDataException
import com.alaishat.ahmed.themoviedb.data.source.remote.exception.ConnectionDataException
import com.alaishat.ahmed.themoviedb.domain.achitecture.exception.DomainException
import com.alaishat.ahmed.themoviedb.domain.achitecture.exception.FetchFailedDomainException
import com.alaishat.ahmed.themoviedb.domain.achitecture.exception.UnknownDomainException

/**
 * Created by Ahmed Al-Aishat on Sep/16/2023.
 * The Movie DB Project.
 */
fun Throwable?.toDomainException(): DomainException = when (this) {
    is ApiDataException -> FetchFailedDomainException(this)
    is ConnectionDataException -> FetchFailedDomainException(this)
    else -> UnknownDomainException(this)
}