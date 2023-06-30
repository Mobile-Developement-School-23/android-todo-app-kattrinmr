package com.katerina.todoapp.data.api.responses

import com.katerina.todoapp.data.models.TaskResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class UpdateTasksRequestWrapper(
    @SerialName("list")
    val tasks: List<TaskResponse>
)