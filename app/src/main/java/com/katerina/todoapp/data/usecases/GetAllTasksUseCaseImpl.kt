package com.katerina.todoapp.data.usecases

import com.katerina.todoapp.data.repositories.ToDoRepository
import com.katerina.todoapp.domain.usecases.GetAllTasksUseCase
import javax.inject.Inject

class GetAllTasksUseCaseImpl @Inject constructor(
    private val toDoRepository: ToDoRepository
) : GetAllTasksUseCase {
    override fun invoke() = toDoRepository.getAllTasks()
}