package com.katerina.todoapp.presentation.elm.reducers

import com.katerina.todoapp.domain.models.TaskModel
import com.katerina.todoapp.presentation.base.extensions.makeTaskDone
import com.katerina.todoapp.presentation.elm.models.TasksListCommand
import com.katerina.todoapp.presentation.elm.models.TasksListEffect
import com.katerina.todoapp.presentation.elm.models.TasksListEvent
import com.katerina.todoapp.presentation.elm.models.TasksListState
import com.katerina.todoapp.presentation.elm.models.TasksListStatus
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer

class TasksListReducer
    : DslReducer<TasksListEvent, TasksListState, TasksListEffect, TasksListCommand>() {

    override fun Result.reduce(event: TasksListEvent) = when (event) {

        is TasksListEvent.Ui.Init -> {
            when (state.tasksListStatus) {

                is TasksListStatus.Loading -> {
                    commands { +TasksListCommand.GetAllTasks }
                }

                else -> Unit
            }
        }

        is TasksListEvent.Ui.AddNewTaskClicked -> {
            effects { +TasksListEffect.NavigateToCreateNewTask }
        }

        is TasksListEvent.Ui.OnTaskCheckboxClicked -> {
            commands {
                +TasksListCommand.ChangeTaskStatus(
                    state.tasks ?: listOf(),
                    event.taskId,
                    event.status
                )
            }
        }

        is TasksListEvent.Ui.OnTaskDragged -> {
            state { copy(tasks = event.tasks) }
        }

        is TasksListEvent.Ui.OnTaskSwipedToBeDone -> {
            val tasks = state.tasks?.makeTaskDone(event.task) ?: state.tasks
            state { copy(tasks = tasks, doneTasksCount = getDoneTasksCount(tasks)) }
        }

        is TasksListEvent.Ui.OnTaskSwipedToBeRemoved -> {
            state { copy(tasks = event.tasks, doneTasksCount = getDoneTasksCount(event.tasks)) }
        }

        is TasksListEvent.Ui.OnShowOrHideDoneTasksBtnClicked -> {
            state { copy(isShowingDoneTasks = !state.isShowingDoneTasks) }
        }

        is TasksListEvent.Internal.LoadAllTasksSuccess -> {
            state {
                copy(
                    tasks = event.tasks,
                    tasksListStatus = TasksListStatus.ShowingTasks,
                    doneTasksCount = getDoneTasksCount(event.tasks)
                )
            }
        }

        is TasksListEvent.Internal.Error -> {
            state { copy(tasksListStatus = TasksListStatus.Failure) }
            effects { +TasksListEffect.ShowSystemMessage(event.errorMessage) }
        }
    }

    private fun getDoneTasksCount(tasks: List<TaskModel>?): Int =
        tasks?.count { it.isDone } ?: 0
}