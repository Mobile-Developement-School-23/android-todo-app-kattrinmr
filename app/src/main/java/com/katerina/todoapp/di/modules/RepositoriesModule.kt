package com.katerina.todoapp.di.modules

import com.katerina.todoapp.data.repositories.ToDoRepositoryImpl
import com.katerina.todoapp.domain.repositories.ToDoRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface RepositoriesModule {
    @Binds
    @Singleton
    fun bindToDoRepository(impl: ToDoRepositoryImpl): ToDoRepository
}