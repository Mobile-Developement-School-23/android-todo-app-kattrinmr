package com.katerina.todoapp.domain.repositories

import com.katerina.todoapp.data.results.RepositoryResult
import com.katerina.todoapp.domain.models.TaskModel
import kotlinx.coroutines.flow.Flow

interface ToDoRepository {
    fun getAllTasks(): Flow<RepositoryResult<List<TaskModel>>>
    fun addTask(task: TaskModel): Flow<RepositoryResult<List<TaskModel>>>
    fun removeTask(task: TaskModel): Flow<RepositoryResult<List<TaskModel>>>
    fun editTask(task: TaskModel): Flow<RepositoryResult<List<TaskModel>>>
}