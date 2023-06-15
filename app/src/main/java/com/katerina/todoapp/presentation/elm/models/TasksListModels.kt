package com.katerina.todoapp.presentation.elm.models

import com.katerina.todoapp.domain.models.TaskModel
import com.katerina.todoapp.domain.utils.TaskImportance

data class TasksListState(
    val tasks: List<TaskModel>? = null,
    val doneTasksCount: Int = 0,
    val isShowingDoneTasks: Boolean = false,
    val tasksListStatus: TasksListStatus = TasksListStatus.Loading
)

sealed interface TasksListStatus {
    object Loading : TasksListStatus
    object Failure : TasksListStatus
    object ShowingTasks : TasksListStatus
    data class ShowingTaskDescription(
        val task: TaskModel? = null,
        val importance: TaskImportance = TaskImportance.LOW,
        val deadlineDateTimestamp: Long? = null
    ) : TasksListStatus
}

sealed interface TasksListEvent {

    sealed interface Ui : TasksListEvent {
        object Init : Ui
        object AddNewTaskClicked : Ui
        data class OnTaskClicked(val taskId: String, val task: TaskModel) : Ui
        data class OnTaskCheckboxClicked(val taskId: String, val status: Boolean) : Ui
        data class OnTaskSwipedToBeDone(val task: TaskModel) : Ui
        data class OnTaskSwipedToBeRemoved(val task: TaskModel) : Ui
        data class OnTaskDragged(val tasks: List<TaskModel>) : Ui
        object OnShowOrHideDoneTasksBtnClicked : Ui
        data class OnDeadlineDateClicked(val timestamp: Long?) : Ui
        data class OnImportanceSelected(val importance: TaskImportance) : Ui
        object OnCloseBtnClicked : Ui
        data class OnSaveBtnClicked(val taskDescription: String) : Ui
        object OnDeleteTaskBtnClicked : Ui
    }

    sealed interface Internal : TasksListEvent {
        data class LoadAllTasksSuccess(val tasks: List<TaskModel>) : Internal
        data class AddTaskSuccess(val task: TaskModel) : Internal
        data class Error(val errorMessage: String) : Internal
    }
}

sealed interface TasksListEffect {
    data class ShowSystemMessage(val message: String) : TasksListEffect
    data class NavigateToTaskDescriptionScreen(val taskId: String?) : TasksListEffect
    object NavigateToTaskListScreen : TasksListEffect
    object ScrollToBeginningOfList : TasksListEffect
}

sealed interface TasksListCommand {
    object GetAllTasks : TasksListCommand
    data class AddTask(
        val text: String,
        val importance: TaskImportance,
        val deadlineDateTimestamp: Long?
    ) : TasksListCommand

    data class ChangeTaskStatus(
        val tasks: List<TaskModel>,
        val taskId: String,
        val status: Boolean
    ) : TasksListCommand
}