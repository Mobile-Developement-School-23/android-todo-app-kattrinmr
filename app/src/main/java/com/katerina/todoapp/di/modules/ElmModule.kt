package com.katerina.todoapp.di.modules

import com.katerina.todoapp.presentation.elm.TasksListStoreHolder
import com.katerina.todoapp.presentation.elm.actors.TasksListActor
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ElmModule {
    @Provides
    @Singleton
    fun provideTasksListStoreHolder(actor: TasksListActor) = TasksListStoreHolder(actor)
}