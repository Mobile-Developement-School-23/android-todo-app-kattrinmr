package com.katerina.todoapp.data.datasources

import com.katerina.todoapp.data.extensions.safeCall
import com.katerina.todoapp.data.mappers.toEntity
import com.katerina.todoapp.data.mappers.toModel
import com.katerina.todoapp.data.results.DataSourceResult
import com.katerina.todoapp.data.room.daos.TasksDao
import com.katerina.todoapp.domain.datasources.TasksDataSource
import com.katerina.todoapp.domain.models.TaskModel
import javax.inject.Inject

class RoomTasksDataSource @Inject constructor(
    private val tasksDao: TasksDao
) : TasksDataSource {

    override fun getAllTasks() = safeCall {
        tasksDao.getAllTasks()
            .map { task -> task.toModel() }
            .also { tasks -> emit(DataSourceResult.Success(tasks)) }
    }

    override fun addTask(task: TaskModel) = safeCall {
        tasksDao.insertTask(task.toEntity())

        getAllTasks().collect { result ->
            if (result is DataSourceResult.Success) {
                emit(DataSourceResult.Success(result.data))
            }
        }
    }

    override fun removeTask(task: TaskModel) = safeCall {
        tasksDao.deleteTaskById(task.id)

        getAllTasks().collect { result ->
            if (result is DataSourceResult.Success) {
                emit(DataSourceResult.Success(result.data))
            }
        }
    }

    override fun editTask(task: TaskModel) = safeCall {
        addTask(task).collect { result ->
            if (result is DataSourceResult.Success) {
                emit(DataSourceResult.Success(result.data))
            }
        }
    }
}