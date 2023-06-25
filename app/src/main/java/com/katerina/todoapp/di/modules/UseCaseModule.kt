package com.katerina.todoapp.di.modules

import com.katerina.todoapp.data.usecases.AddTaskUseCaseImpl
import com.katerina.todoapp.data.usecases.EditTaskUseCaseImpl
import com.katerina.todoapp.data.usecases.GetAllTasksUseCaseImpl
import com.katerina.todoapp.data.usecases.RemoveTaskUseCaseImpl
import com.katerina.todoapp.domain.usecases.AddTaskUseCase
import com.katerina.todoapp.domain.usecases.EditTaskUseCase
import com.katerina.todoapp.domain.usecases.GetAllTasksUseCase
import com.katerina.todoapp.domain.usecases.RemoveTaskUseCase
import dagger.Binds
import dagger.Module

@Module
interface UseCaseModule {
    @Binds
    fun bindGetAllTasksUseCase(impl: GetAllTasksUseCaseImpl): GetAllTasksUseCase

    @Binds
    fun bindAddTaskUseCase(impl: AddTaskUseCaseImpl): AddTaskUseCase

    @Binds
    fun bindRemoveTaskUseCase(impl: RemoveTaskUseCaseImpl): RemoveTaskUseCase

    @Binds
    fun bindEditTaskUseCase(impl: EditTaskUseCaseImpl): EditTaskUseCase
}