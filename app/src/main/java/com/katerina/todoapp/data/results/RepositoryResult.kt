package com.katerina.todoapp.data.results

sealed class RepositoryResult<out T : Any> {
    data class Success<out T : Any>(val data: T) : RepositoryResult<T>()
    data class Error(val message: String) : RepositoryResult<Nothing>()
}