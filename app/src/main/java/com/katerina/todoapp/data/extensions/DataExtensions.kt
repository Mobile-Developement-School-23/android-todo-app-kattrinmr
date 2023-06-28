package com.katerina.todoapp.data.extensions

import com.katerina.todoapp.data.results.DataSourceResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow

internal fun <T : Any> safeCall(call: suspend FlowCollector<DataSourceResult<T>>.() -> Unit)
        : Flow<DataSourceResult<T>> = flow {
    try {
        call()
    } catch (e: Exception) {
        emit(DataSourceResult.Error("An unknown error has occurred"))
    }
}