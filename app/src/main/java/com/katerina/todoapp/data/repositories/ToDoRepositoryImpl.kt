package com.katerina.todoapp.data.repositories

import com.katerina.todoapp.data.results.RepositoryResult
import com.katerina.todoapp.data.utils.tasksListStub
import com.katerina.todoapp.domain.models.TaskModel
import com.katerina.todoapp.domain.repositories.ToDoRepository
import kotlinx.coroutines.flow.flow

class ToDoRepositoryImpl : ToDoRepository {

    override fun getAllTasks() = flow {
        emit(RepositoryResult.Success(tasksListStub))
    }

    override fun changeTaskStatus(
        tasks: List<TaskModel>,
        taskId: String,
        status: Boolean
    ) = flow {

        tasks
            .indexOfFirst { it.id == taskId }
            .also { index ->
                if (index != -1) {
                    tasks.toMutableList()
                        .apply { this[index] = this[index].copy(isDone = status) }
                        .also { emit(RepositoryResult.Success(it.toList())) }
                }
            }
    }
}