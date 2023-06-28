package com.katerina.todoapp.domain.datasources

import com.katerina.todoapp.data.results.DataSourceResult
import com.katerina.todoapp.domain.models.TaskModel
import kotlinx.coroutines.flow.Flow

interface TasksDataSource {
    fun getAllTasks(): Flow<DataSourceResult<List<TaskModel>>>
    fun addTask(task: TaskModel): Flow<DataSourceResult<List<TaskModel>>>
    fun removeTask(task: TaskModel): Flow<DataSourceResult<List<TaskModel>>>
    fun editTask(task: TaskModel): Flow<DataSourceResult<List<TaskModel>>>
}