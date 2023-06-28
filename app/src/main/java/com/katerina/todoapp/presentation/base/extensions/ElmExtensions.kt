package com.katerina.todoapp.presentation.base.extensions

import com.katerina.todoapp.data.results.DataSourceResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun <T : Any, R : Any> Flow<DataSourceResult<T>>.mapResultEvents(
    eventMapper: (T) -> R,
    errorMapper: (errorMessage: String) -> R
): Flow<R> {
    return this.map { result ->
        when (result) {
            is DataSourceResult.Success -> eventMapper(result.data)
            is DataSourceResult.Error -> errorMapper(result.message)
        }
    }
}