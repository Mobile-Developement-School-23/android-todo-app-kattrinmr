package com.katerina.todoapp.di

import android.content.Context
import com.katerina.todoapp.di.modules.ElmModule
import com.katerina.todoapp.di.modules.NetworkModule
import com.katerina.todoapp.di.modules.RoomModule
import com.katerina.todoapp.di.modules.UseCaseModule
import com.katerina.todoapp.presentation.fragments.TaskDescriptionFragment
import com.katerina.todoapp.presentation.fragments.TasksListFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [RoomModule::class, NetworkModule::class, UseCaseModule::class, ElmModule::class]
)
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance
            context: Context
        ): AppComponent
    }

    fun inject(fragment: TasksListFragment)
    fun inject(fragment: TaskDescriptionFragment)
}