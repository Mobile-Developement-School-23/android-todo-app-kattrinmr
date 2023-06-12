package com.katerina.todoapp.domain.usecases

import com.katerina.todoapp.data.results.RepositoryResult
import com.katerina.todoapp.domain.models.TaskModel
import kotlinx.coroutines.flow.Flow

interface ChangeTaskStatusUseCase {
    operator fun invoke(tasks: List<TaskModel>, taskId: String, status: Boolean):
            Flow<RepositoryResult<List<TaskModel>>>
}