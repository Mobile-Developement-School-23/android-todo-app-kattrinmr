package com.katerina.todoapp.data.results

sealed class DataSourceResult<out T : Any> {
    data class Success<out T : Any>(val data: T) : DataSourceResult<T>()
    data class Error(val message: String) : DataSourceResult<Nothing>()
}