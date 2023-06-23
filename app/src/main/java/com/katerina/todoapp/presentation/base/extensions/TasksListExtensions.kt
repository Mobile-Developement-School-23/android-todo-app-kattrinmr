package com.katerina.todoapp.presentation.base.extensions

import android.content.Context
import android.os.Build
import com.katerina.todoapp.domain.models.TaskModel
import com.katerina.todoapp.domain.utils.TaskImportance
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun List<TaskModel>.makeTaskDone(task: TaskModel): List<TaskModel> {
    val tasks = this.toMutableList()
    val index = tasks.indexOfFirst { it.id == task.id }

    if (index != -1) {
        tasks[index] = task
    }

    return tasks.toList()
}

fun List<TaskModel>.removeTask(task: TaskModel): List<TaskModel> {
    val tasks = this.toMutableList()
    val index = tasks.indexOfFirst { it.id == task.id }

    if (index != -1) {
        tasks.removeAt(index)
    }

    return tasks.toList()
}

fun List<TaskModel>.addTask(task: TaskModel): List<TaskModel> {
    val tasks = this.toMutableList()
    tasks.add(0, task)

    return tasks.toList()
}

fun List<TaskModel>.editTask(
    taskId: String,
    text: String,
    importance: TaskImportance,
    deadlineDateTimestamp: Long?
): List<TaskModel> {
    val tasks = this.toMutableList()
    val index = tasks.indexOfFirst { it.id == taskId }

    if (index != -1) {
        val task = tasks[index]

        val editedTask = task.copy(
            text = text,
            importance = importance,
            deadlineDateTimestamp = deadlineDateTimestamp
        )

        tasks[index] = editedTask
    }

    return tasks
}

fun List<TaskModel>.findMaxIntId(): Int =
    maxByOrNull { it.id.toIntOrNull() ?: -1 }?.id?.toIntOrNull() ?: -1

fun Long.toDateFormat(context: Context): String = when {
    Build.VERSION.SDK_INT >= 26 -> {
        val instant = Instant.ofEpochMilli(this)
        val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        localDateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
    }

    else -> {
        val date = Date(this)
        val locale = context.resources.configuration.locales.get(0)
        val formatter = SimpleDateFormat("dd.MM.yyyy", locale)

        formatter.timeZone = TimeZone.getDefault()
        formatter.format(date)
    }
}

fun createDateString(day: Int, month: Int, year: Int): String =
    String.format("%02d.%02d.%04d", day, month + 1, year)

fun dateStringToTimestamp(dateString: String): Long {
    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    val date = dateFormat.parse(dateString)
    return date?.time ?: 0L
}