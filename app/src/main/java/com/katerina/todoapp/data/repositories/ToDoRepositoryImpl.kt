package com.katerina.todoapp.data.repositories

import com.katerina.todoapp.data.results.RepositoryResult
import com.katerina.todoapp.data.utils.tasksListStub
import com.katerina.todoapp.domain.models.TaskModel
import com.katerina.todoapp.domain.repositories.ToDoRepository
import com.katerina.todoapp.domain.utils.TaskImportance
import com.katerina.todoapp.presentation.base.extensions.findMaxIntId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ToDoRepositoryImpl : ToDoRepository {

    override fun getAllTasks(): Flow<RepositoryResult<List<TaskModel>>> = flow {
        emit(RepositoryResult.Success(tasksListStub))
    }

    override fun addTask(
        text: String,
        importance: TaskImportance,
        deadlineDateTimestamp: Long?
    ) = flow {

        getAllTasks().collect { result ->
            when (result) {

                is RepositoryResult.Success -> {
                    // Временная реализация, пока не будет ясно, как формируются id
                    val id = (result.data.findMaxIntId() + 1).toString()
                    val creationDateTimestamp = System.currentTimeMillis()

                    emit(
                        RepositoryResult.Success(
                            TaskModel(
                                id = id,
                                text = text,
                                importance = importance,
                                isDone = false,
                                creationDateTimestamp = creationDateTimestamp,
                                changeDateTimestamp = creationDateTimestamp,
                                deadlineDateTimestamp = deadlineDateTimestamp
                            )
                        )
                    )
                }

                is RepositoryResult.Error -> emit(RepositoryResult.Error(result.message))
            }
        }
    }

    override fun changeTaskStatus(
        tasks: List<TaskModel>,
        taskId: String,
        status: Boolean
    ) = flow {

        tasks.indexOfFirst { it.id == taskId }
            .also { index ->
                if (index != -1) {
                    tasks.toMutableList()
                        .apply { this[index] = this[index].copy(isDone = status) }
                        .also { emit(RepositoryResult.Success(it.toList())) }
                }
            }
    }
}