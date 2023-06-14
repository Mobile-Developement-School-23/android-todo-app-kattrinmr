package com.katerina.todoapp.presentation.base.mappers

import com.katerina.todoapp.domain.models.TaskModel

fun List<TaskModel>.toUndoneTasks(): List<TaskModel> = this.filter { !it.isDone }