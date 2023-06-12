package com.katerina.todoapp.domain.models

import com.katerina.todoapp.domain.utils.TaskImportance

data class TaskModel(
    val id: String,
    val text: String,
    val importance: TaskImportance,
    val isDone: Boolean,
    val creationDateTimestamp: Long,
    val changeDateTimestamp: Long,
    val deadlineDateTimestamp: Long? = null
)
