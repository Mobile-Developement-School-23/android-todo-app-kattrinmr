package com.katerina.todoapp.domain.usecases

import com.katerina.todoapp.data.results.RepositoryResult
import com.katerina.todoapp.domain.models.TaskModel
import kotlinx.coroutines.flow.Flow

interface RemoveTaskUseCase {
    operator fun invoke(task: TaskModel): Flow<RepositoryResult<List<TaskModel>>>
}