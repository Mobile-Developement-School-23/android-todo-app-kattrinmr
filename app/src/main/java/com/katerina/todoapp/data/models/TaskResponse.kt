package com.katerina.todoapp.data.models

import com.katerina.todoapp.domain.utils.TaskImportance
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class TaskResponse(
    @SerialName("id")
    val id: String,
    @SerialName("text")
    val text: String,
    @SerialName("importance")
    val importance: TaskImportance,
    @SerialName("deadline")
    val deadlineDateTimestamp: Long? = null,
    @SerialName("done")
    val isDone: Boolean,
    @SerialName("color")
    val color: String? = null,
    @SerialName("created_at")
    val creationDateTimestamp: Long,
    @SerialName("changed_at")
    val changeDateTimestamp: Long,
    @SerialName("last_updated_by")
    val lastUpdatedBy: String
)