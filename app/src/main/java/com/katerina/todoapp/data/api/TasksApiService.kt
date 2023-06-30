package com.katerina.todoapp.data.api

import com.katerina.todoapp.data.api.responses.TasksListResponseWrapper
import com.katerina.todoapp.data.api.responses.UpdateTasksRequestWrapper
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH

interface TasksApiService {
    @GET("list")
    suspend fun getAllTasks(): TasksListResponseWrapper

    @PATCH("list")
    suspend fun updateTasks(
        @Header("X-Last-Known-Revision") revision: Int,
        @Body tasks: UpdateTasksRequestWrapper
    ): TasksListResponseWrapper
}