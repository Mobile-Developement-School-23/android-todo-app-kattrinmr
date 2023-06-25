package com.katerina.todoapp.di

import com.katerina.todoapp.di.modules.ElmModule
import com.katerina.todoapp.di.modules.RepositoriesModule
import com.katerina.todoapp.di.modules.UseCaseModule
import com.katerina.todoapp.presentation.fragments.TaskDescriptionFragment
import com.katerina.todoapp.presentation.fragments.TasksListFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RepositoriesModule::class, UseCaseModule::class, ElmModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(): AppComponent
    }

    fun inject(fragment: TasksListFragment)
    fun inject(fragment: TaskDescriptionFragment)
}