package com.katerina.todoapp.presentation.elm.models

import com.katerina.todoapp.domain.models.TaskModel

data class TasksListState(
    val tasks: List<TaskModel>? = null,
    val doneTasksCount: Int = 0,
    val tasksListStatus: TasksListStatus = TasksListStatus.Loading
)

sealed interface TasksListStatus {
    object Loading : TasksListStatus
    object Failure : TasksListStatus
    object ShowingTasks : TasksListStatus
}

sealed interface TasksListEvent {

    sealed interface Ui : TasksListEvent {
        object Init : Ui
        object AddNewTaskClicked : Ui
        data class OnTaskCheckboxClicked(val taskId: String, val status: Boolean) : Ui
        data class OnTaskDraggedOrSwiped(val tasks: List<TaskModel>) : Ui
    }

    sealed interface Internal : TasksListEvent {
        data class LoadAllTasksSuccess(val tasks: List<TaskModel>) : Internal
        data class Error(val errorMessage: String) : Internal
    }
}

sealed interface TasksListEffect {
    data class ShowSystemMessage(val message: String) : TasksListEffect
    data class NavigateToEditTask(val taskId: String) : TasksListEffect
    object NavigateToCreateNewTask : TasksListEffect
}

sealed interface TasksListCommand {
    object GetAllTasks : TasksListCommand

    data class ChangeTaskStatus(
        val tasks: List<TaskModel>,
        val taskId: String,
        val status: Boolean
    ) : TasksListCommand
}