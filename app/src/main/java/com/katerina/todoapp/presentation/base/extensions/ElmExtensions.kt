package com.katerina.todoapp.presentation.base.extensions

import com.katerina.todoapp.data.results.RepositoryResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun <T : Any, R : Any> Flow<RepositoryResult<T>>.mapResultEvents(
    eventMapper: (T) -> R,
    errorMapper: (errorMessage: String) -> R
): Flow<R> {
    return this.map { result ->
        when (result) {
            is RepositoryResult.Success -> eventMapper(result.data)
            is RepositoryResult.Error -> errorMapper(result.message)
        }
    }
}