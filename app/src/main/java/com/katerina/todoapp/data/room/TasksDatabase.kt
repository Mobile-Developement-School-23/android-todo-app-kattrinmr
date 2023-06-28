package com.katerina.todoapp.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.katerina.todoapp.data.room.daos.TasksDao
import com.katerina.todoapp.data.room.entities.TaskEntity

@Database(entities = [TaskEntity::class], version = 1)
abstract class TasksDatabase : RoomDatabase() {
    abstract fun tasksDao(): TasksDao
}