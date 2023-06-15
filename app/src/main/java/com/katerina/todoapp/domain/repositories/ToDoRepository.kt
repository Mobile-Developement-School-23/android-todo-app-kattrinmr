package com.katerina.todoapp.domain.repositories

import com.katerina.todoapp.data.results.RepositoryResult
import com.katerina.todoapp.domain.models.TaskModel
import com.katerina.todoapp.domain.utils.TaskImportance
import kotlinx.coroutines.flow.Flow

interface ToDoRepository {
    fun getAllTasks(): Flow<RepositoryResult<List<TaskModel>>>

    fun addTask(
        text: String,
        importance: TaskImportance,
        deadlineDateTimestamp: Long?
    ): Flow<RepositoryResult<TaskModel>>

    fun changeTaskStatus(
        tasks: List<TaskModel>,
        taskId: String,
        status: Boolean
    ): Flow<RepositoryResult<List<TaskModel>>>
}