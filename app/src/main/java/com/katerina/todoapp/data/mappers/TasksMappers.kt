package com.katerina.todoapp.data.mappers

import com.katerina.todoapp.data.models.TaskResponse
import com.katerina.todoapp.data.room.entities.TaskEntity
import com.katerina.todoapp.domain.models.TaskModel

fun TaskEntity.toModel(): TaskModel =
    TaskModel(
        id = this.id,
        text = this.text,
        importance = this.importance,
        isDone = this.isDone,
        creationDateTimestamp = this.creationDateTimestamp,
        changeDateTimestamp = this.changeDateTimestamp,
        deadlineDateTimestamp = this.deadlineDateTimestamp
    )

fun TaskModel.toEntity(): TaskEntity =
    TaskEntity(
        id = this.id,
        text = this.text,
        importance = this.importance,
        isDone = this.isDone,
        creationDateTimestamp = this.creationDateTimestamp,
        changeDateTimestamp = this.changeDateTimestamp,
        deadlineDateTimestamp = this.deadlineDateTimestamp
    )

fun TaskResponse.toModel(): TaskModel =
    TaskModel(
        id = this.id,
        text = this.text,
        importance = this.importance,
        isDone = this.isDone,
        creationDateTimestamp = this.creationDateTimestamp,
        changeDateTimestamp = this.changeDateTimestamp,
        deadlineDateTimestamp = this.deadlineDateTimestamp
    )

fun TaskModel.toResponse(): TaskResponse =
    TaskResponse(
        id = this.id,
        text = this.text,
        importance = this.importance,
        isDone = this.isDone,
        color = null,
        creationDateTimestamp = this.creationDateTimestamp,
        changeDateTimestamp = this.changeDateTimestamp,
        deadlineDateTimestamp = this.deadlineDateTimestamp,
        lastUpdatedBy = ""
    )