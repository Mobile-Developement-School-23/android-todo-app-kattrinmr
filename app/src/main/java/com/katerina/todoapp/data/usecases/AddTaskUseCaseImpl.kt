package com.katerina.todoapp.data.usecases

import com.katerina.todoapp.data.repositories.ToDoRepository
import com.katerina.todoapp.domain.models.TaskModel
import com.katerina.todoapp.domain.usecases.AddTaskUseCase
import javax.inject.Inject

class AddTaskUseCaseImpl @Inject constructor(
    private val toDoRepository: ToDoRepository
) : AddTaskUseCase {
    override fun invoke(task: TaskModel) = toDoRepository.addTask(task)
}