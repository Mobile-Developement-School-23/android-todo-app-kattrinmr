package com.katerina.todoapp.presentation.elm.reducers

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

        is TasksListEvent.Internal.LoadAllTasksSuccess -> {
            state { copy(tasks = event.tasks, tasksListStatus = TasksListStatus.ShowingTasks) }

            val count = state.tasks?.count { it.isDone } ?: 0
            state { copy(doneTasksCount = count)}
        }

        is TasksListEvent.Internal.Error -> {
            state { copy(tasksListStatus = TasksListStatus.Failure) }
            effects { +TasksListEffect.ShowSystemMessage(event.errorMessage) }
        }
    }
}