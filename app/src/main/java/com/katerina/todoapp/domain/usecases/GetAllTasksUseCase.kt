package com.katerina.todoapp.domain.usecases

import com.katerina.todoapp.data.results.RepositoryResult
import com.katerina.todoapp.domain.models.TaskModel
import kotlinx.coroutines.flow.Flow

interface GetAllTasksUseCase {
    operator fun invoke(): Flow<RepositoryResult<List<TaskModel>>>
}