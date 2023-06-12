package com.katerina.todoapp.data.usecases

import com.katerina.todoapp.data.repositories.ToDoRepositoryImpl
import com.katerina.todoapp.domain.repositories.ToDoRepository
import com.katerina.todoapp.domain.usecases.GetAllTasksUseCase

class GetAllTasksUseCaseImpl : GetAllTasksUseCase {

    private val toDoRepository: ToDoRepository by lazy { ToDoRepositoryImpl() }

    override fun invoke() = toDoRepository.getAllTasks()
}