package com.katerina.todoapp.data.repositories

import com.katerina.todoapp.data.datasources.RoomTasksDataSource
import com.katerina.todoapp.data.results.DataSourceResult
import com.katerina.todoapp.domain.datasources.TasksDataSource
import com.katerina.todoapp.domain.models.TaskModel
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ToDoRepository @Inject constructor(
    private val roomTasksDataSource: RoomTasksDataSource
) : TasksDataSource {
    override fun getAllTasks() = roomTasksDataSource.getAllTasks()

    override fun addTask(task: TaskModel): Flow<DataSourceResult<List<TaskModel>>> {
        val id = UUID.randomUUID().toString()
        val creationDateTimestamp = System.currentTimeMillis()

        return roomTasksDataSource.addTask(
            task.copy(
                id = id,
                creationDateTimestamp = creationDateTimestamp,
                changeDateTimestamp = creationDateTimestamp
            )
        )
    }

    override fun removeTask(task: TaskModel) = roomTasksDataSource.removeTask(task)

    override fun editTask(task: TaskModel): Flow<DataSourceResult<List<TaskModel>>> {
        val changeDateTimestamp = System.currentTimeMillis()

        return roomTasksDataSource.addTask(task.copy(changeDateTimestamp = changeDateTimestamp))
    }
}