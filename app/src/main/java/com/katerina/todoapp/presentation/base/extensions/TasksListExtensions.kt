package com.katerina.todoapp.presentation.base.extensions

import com.katerina.todoapp.domain.models.TaskModel

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