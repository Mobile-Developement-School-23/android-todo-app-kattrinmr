package com.katerina.todoapp.data.usecases

import com.katerina.todoapp.data.repositories.ToDoRepositoryImpl
import com.katerina.todoapp.domain.models.TaskModel
import com.katerina.todoapp.domain.repositories.ToDoRepository
import com.katerina.todoapp.domain.usecases.ChangeTaskStatusUseCase

class ChangeTaskStatusUseCaseImpl : ChangeTaskStatusUseCase {

    private val toDoRepository: ToDoRepository by lazy { ToDoRepositoryImpl() }

    override fun invoke(tasks: List<TaskModel>, taskId: String, status: Boolean) =
        toDoRepository.changeTaskStatus(tasks, taskId, status)
}