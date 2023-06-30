package com.katerina.todoapp.domain.usecases

import com.katerina.todoapp.data.results.DataSourceResult
import com.katerina.todoapp.domain.models.TaskModel
import kotlinx.coroutines.flow.Flow

interface GetAllTasksUseCase {
    operator fun invoke(): Flow<DataSourceResult<List<TaskModel>>>
}