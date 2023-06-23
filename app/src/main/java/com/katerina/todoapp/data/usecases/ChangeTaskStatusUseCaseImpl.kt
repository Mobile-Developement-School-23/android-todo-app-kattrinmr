package com.katerina.todoapp.data.usecases

import com.katerina.todoapp.data.repositories.ToDoRepositoryImpl
import com.katerina.todoapp.domain.models.TaskModel
import com.katerina.todoapp.domain.repositories.ToDoRepository
import com.katerina.todoapp.domain.usecases.ChangeTaskStatusUseCase

class ChangeTaskStatusUseCaseImpl : ChangeTaskStatusUseCase {

    /**
     * Пока что инициализируется слудующем образом, потом будет инжектиться с помощью даггера
     */
    private val toDoRepository: ToDoRepository by lazy { ToDoRepositoryImpl() }

    override fun invoke(tasks: List<TaskModel>, taskId: String, status: Boolean) =
        toDoRepository.changeTaskStatus(tasks, taskId, status)
}