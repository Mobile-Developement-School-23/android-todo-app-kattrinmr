package com.katerina.todoapp.domain.usecases

import com.katerina.todoapp.data.results.RepositoryResult
import com.katerina.todoapp.domain.models.TaskModel
import com.katerina.todoapp.domain.utils.TaskImportance
import kotlinx.coroutines.flow.Flow

interface AddTaskUseCase {
    operator fun invoke(text: String, importance: TaskImportance, deadlineDateTimestamp: Long?):
            Flow<RepositoryResult<TaskModel>>
}