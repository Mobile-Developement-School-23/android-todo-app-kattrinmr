package com.katerina.todoapp.di.modules

import android.content.Context
import androidx.room.Room
import com.katerina.todoapp.data.room.TasksDatabase
import com.katerina.todoapp.data.room.daos.TasksDao
import com.katerina.todoapp.data.room.utils.TASKS_DATABASE
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule {

    @Singleton
    @Provides
    fun provideTasksDatabase(context: Context): TasksDatabase =
        Room.databaseBuilder(context, TasksDatabase::class.java, TASKS_DATABASE)
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideTasksDao(database: TasksDatabase): TasksDao =
        database.tasksDao()
}