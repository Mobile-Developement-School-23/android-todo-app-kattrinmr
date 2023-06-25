package com.katerina.todoapp.data.repositories

import com.katerina.todoapp.data.results.RepositoryResult
import com.katerina.todoapp.data.utils.tasksListStub
import com.katerina.todoapp.domain.models.TaskModel
import com.katerina.todoapp.domain.repositories.ToDoRepository
import com.katerina.todoapp.presentation.base.extensions.findMaxIntId
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ToDoRepositoryImpl @Inject constructor() : ToDoRepository {

    private var repositoryTasks = tasksListStub

    override fun getAllTasks(): Flow<RepositoryResult<List<TaskModel>>> = flow {
        emit(RepositoryResult.Success(repositoryTasks.toList()))
    }

    override fun addTask(task: TaskModel) = flow {
        val tasks = repositoryTasks.toMutableList()

        val id = tasks.findMaxIntId() + 1
        val creationDateTimestamp = System.currentTimeMillis()

        tasks.add(
            0,
            task.copy(
                id = id.toString(),
                creationDateTimestamp = creationDateTimestamp,
                changeDateTimestamp = creationDateTimestamp
            )
        )

        repositoryTasks = tasks.toList().also {
            emit(RepositoryResult.Success(it))
        }
    }

    override fun removeTask(task: TaskModel) = flow {
        val tasks = repositoryTasks.toMutableList()
        val index = tasks.indexOfFirst { it.id == task.id }

        if (index != -1) {
            tasks.removeAt(index)
        }

        repositoryTasks = tasks.toList().also {
            emit(RepositoryResult.Success(it))
        }
    }

    override fun editTask(task: TaskModel) = flow {
        val tasks = repositoryTasks.toMutableList()
        val index = tasks.indexOfFirst { it.id == task.id }

        if (index != -1) {
            val uneditedTask = tasks[index]

            val editedTask = uneditedTask.copy(
                id = task.id,
                text = task.text,
                importance = task.importance,
                isDone = task.isDone,
                creationDateTimestamp = task.creationDateTimestamp,
                changeDateTimestamp = System.currentTimeMillis(),
                deadlineDateTimestamp = task.deadlineDateTimestamp
            )

            tasks[index] = editedTask
        }

        repositoryTasks = tasks.toList().also {
            emit(RepositoryResult.Success(it))
        }
    }
}