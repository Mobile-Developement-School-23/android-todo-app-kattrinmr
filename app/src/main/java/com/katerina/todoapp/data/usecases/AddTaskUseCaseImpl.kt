package com.katerina.todoapp.data.usecases

import com.katerina.todoapp.data.repositories.ToDoRepositoryImpl
import com.katerina.todoapp.domain.repositories.ToDoRepository
import com.katerina.todoapp.domain.usecases.AddTaskUseCase
import com.katerina.todoapp.domain.utils.TaskImportance

class AddTaskUseCaseImpl : AddTaskUseCase {

    /**
     * Пока что инициализируется слудующем образом, потом будет инжектиться с помощью даггера
     */
    private val toDoRepository: ToDoRepository by lazy { ToDoRepositoryImpl() }

    override fun invoke(text: String, importance: TaskImportance, deadlineDateTimestamp: Long?) =
        toDoRepository.addTask(text, importance, deadlineDateTimestamp)
}