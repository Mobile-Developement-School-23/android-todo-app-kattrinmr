package com.katerina.todoapp.data.usecases

import com.katerina.todoapp.domain.models.TaskModel
import com.katerina.todoapp.domain.repositories.ToDoRepository
import com.katerina.todoapp.domain.usecases.RemoveTaskUseCase
import javax.inject.Inject

class RemoveTaskUseCaseImpl @Inject constructor(
    private val toDoRepository: ToDoRepository
) : RemoveTaskUseCase {
    override fun invoke(task: TaskModel) = toDoRepository.removeTask(task)
}