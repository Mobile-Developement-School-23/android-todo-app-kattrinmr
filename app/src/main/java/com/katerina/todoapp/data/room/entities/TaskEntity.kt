package com.katerina.todoapp.data.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.katerina.todoapp.data.room.utils.TASKS_TABLE
import com.katerina.todoapp.domain.utils.TaskImportance

@Entity(tableName = TASKS_TABLE)
data class TaskEntity(
    @ColumnInfo("task_id") @PrimaryKey val id: String,
    @ColumnInfo("text") val text: String,
    @ColumnInfo("importance") val importance: TaskImportance,
    @ColumnInfo("is_done") val isDone: Boolean,
    @ColumnInfo("creation_date_timestamp") val creationDateTimestamp: Long,
    @ColumnInfo("change_date_timestamp") val changeDateTimestamp: Long,
    @ColumnInfo("deadline_date_timestamp") val deadlineDateTimestamp: Long?
)