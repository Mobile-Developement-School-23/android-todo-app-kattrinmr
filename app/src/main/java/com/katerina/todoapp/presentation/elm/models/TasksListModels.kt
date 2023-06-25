package com.katerina.todoapp.presentation.elm.models

import com.katerina.todoapp.domain.models.TaskModel
import com.katerina.todoapp.domain.utils.TaskImportance

/**
 * В этом файле описаны модели, которые позволяют реализовать MVI.
 *
 * Более подробно каждый [TasksListEvent] описан в [TasksListReducer][com.katerina.todoapp.presentation.elm.reducers.TasksListReducer].
 *
 *
 * [TasksListState] служит для хранения текущего состояния экрана.
 *
 * @param[tasks] хранит задачи, которые отображаются на экране
 * @param[doneTasksCount] хранит количество выполненных задач
 * @param[isShowingDoneTasks] служит для флага, который отвечает за показ или скрытие выполненных задач
 * @param[tasksListStatus] служит для определения статуса экрана (например, загружаются ли сейчас данные)
 */
data class TasksListState(
    val tasks: List<TaskModel>? = null,
    val doneTasksCount: Int = 0,
    val isShowingDoneTasks: Boolean = false,
    val tasksListStatus: TasksListStatus = TasksListStatus.Loading
)

/**
 * [TasksListStatus] служит для определения статуса экрана.
 */
sealed interface TasksListStatus {
    object Loading : TasksListStatus
    object Failure : TasksListStatus
    object ShowingTasks : TasksListStatus

    /**
     * [ShowingTaskDescription] определяет, открыт ли в данный момент экран с созданием/редактированием задачи.
     *
     * Служит для передачи данных между [TasksListFragment][com.katerina.todoapp.presentation.fragments.TasksListFragment]
     * и [TaskDescriptionFragment][com.katerina.todoapp.presentation.fragments.TaskDescriptionFragment], также у этих фрагментов общий store.
     *
     * @param[task] хранит в себе задачу, которую открыли для редактирования
     * @param[isCreateNewTask] хранит в себе флаг новая задача / редактирование
     */
    data class ShowingTaskDescription(
        val isCreateNewTask: Boolean = false,
        val task: TaskModel? = null
    ) : TasksListStatus
}

/**
 * [TasksListEvent] служит для описания всех событий которые могут произойти на экране.
 *
 * Для удобства все события делятся на [Ui] (приходят от presentation) и на [Internal] (приходят от data).
 */
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

/**
 * [TasksListEffect] служит для описания тех событий, которые необходимо отобразить только один раз.
 *
 * Например, навигация на другой экран или показ снекбара с сообщением.
 */
sealed interface TasksListEffect {
    data class ShowSystemMessage(val message: String) : TasksListEffect
    data class NavigateToTaskDescriptionScreen(val taskId: String?) : TasksListEffect
    object NavigateToTaskListScreen : TasksListEffect
    object ScrollToBeginningOfList : TasksListEffect
}

/**
 * [TasksListCommand] служит для описания тех команд, которые необходимо выполнить.
 *
 * Например, [GetAllTasks] служит для получения всех задач.
 */
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