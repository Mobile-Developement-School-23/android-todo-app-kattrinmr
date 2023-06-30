package com.katerina.todoapp.data.datasources

import android.content.Context
import androidx.preference.PreferenceManager
import com.katerina.todoapp.data.api.TasksApiService
import com.katerina.todoapp.data.api.responses.UpdateTasksRequestWrapper
import com.katerina.todoapp.data.extensions.safeCall
import com.katerina.todoapp.data.mappers.toModel
import com.katerina.todoapp.data.mappers.toResponse
import com.katerina.todoapp.data.results.DataSourceResult
import com.katerina.todoapp.data.utils.KEY_REVISION
import com.katerina.todoapp.domain.datasources.TasksDataSource
import com.katerina.todoapp.domain.models.TaskModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ApiTasksDataSource @Inject constructor(
    private val apiService: TasksApiService,
    private val context: Context
) : TasksDataSource {

    override fun getAllTasks() = safeCall {
        val tasks = apiService.getAllTasks().tasks.map { task -> task.toModel() }
        emit(DataSourceResult.Success(tasks))
    }

    fun updateTasksList(tasks: List<TaskModel>): Flow<DataSourceResult<List<TaskModel>>> = flow {
        val pref = PreferenceManager.getDefaultSharedPreferences(context)
        val revision = pref.getInt(KEY_REVISION, 0)

        val response = apiService.updateTasks(
            revision,
            UpdateTasksRequestWrapper(tasks.map { task -> task.toResponse() })
        )

        pref.edit().apply {
            putInt(KEY_REVISION, response.revision)
            apply()
        }

        emit(DataSourceResult.Success(response.tasks.map { task -> task.toModel() }))
    }

    override fun addTask(task: TaskModel): Flow<DataSourceResult<List<TaskModel>>> {
        TODO("Not yet implemented")
    }

    override fun removeTask(task: TaskModel): Flow<DataSourceResult<List<TaskModel>>> {
        TODO("Not yet implemented")
    }

    override fun editTask(task: TaskModel): Flow<DataSourceResult<List<TaskModel>>> {
        TODO("Not yet implemented")
    }
}