package com.katerina.todoapp.data.room.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.katerina.todoapp.data.room.entities.TaskEntity
import com.katerina.todoapp.data.room.utils.TASKS_TABLE

@Dao
interface TasksDao {
    @Query("SELECT * FROM $TASKS_TABLE")
    fun getAllTasks(): List<TaskEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(task: TaskEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTasks(tasks: List<TaskEntity>)

    @Query("DELETE FROM $TASKS_TABLE WHERE task_id = :taskId")
    fun deleteTaskById(taskId: String)
}