package com.katerina.todoapp.data.extensions

import com.katerina.todoapp.data.api.exceptions.NoConnectivityException
import com.katerina.todoapp.data.results.DataSourceResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import retrofit2.HttpException

internal fun <T : Any> safeCall(call: suspend FlowCollector<DataSourceResult<T>>.() -> Unit)
        : Flow<DataSourceResult<T>> = flow {
    try {
        call()
    } catch (e: NoConnectivityException) {
        emit(DataSourceResult.Error(e.message))
    } catch (e: HttpException) {
        val error = e.response()?.errorBody()?.string()
            ?.let { Json.parseToJsonElement(it).jsonObject }

        val message =
            error?.get("msg")?.jsonPrimitive?.content ?: "An unknown error has occurred"

        emit(DataSourceResult.Error(message))
    } catch (e: Exception) {
        emit(DataSourceResult.Error("An unknown error has occurred"))
    }
}