package com.katerina.todoapp.data.room.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.katerina.todoapp.data.room.entities.TaskEntity
import com.katerina.todoapp.data.room.utils.TASKS_TABLE

@Dao
abstract class TasksDao {
    @Query("SELECT * FROM $TASKS_TABLE")
    abstract fun getAllTasks(): List<TaskEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertTask(task: TaskEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertTasks(tasks: List<TaskEntity>)

    @Query("DELETE FROM $TASKS_TABLE WHERE task_id = :taskId")
    abstract fun deleteTaskById(taskId: String)
}