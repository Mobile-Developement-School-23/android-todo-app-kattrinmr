package com.katerina.todoapp.data.usecases

import com.katerina.todoapp.domain.models.TaskModel
import com.katerina.todoapp.domain.repositories.ToDoRepository
import com.katerina.todoapp.domain.usecases.EditTaskUseCase
import javax.inject.Inject

class EditTaskUseCaseImpl @Inject constructor(
    private val toDoRepository: ToDoRepository
) : EditTaskUseCase {
    override fun invoke(task: TaskModel) = toDoRepository.editTask(task)
}