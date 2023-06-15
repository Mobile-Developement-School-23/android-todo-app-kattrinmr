package com.katerina.todoapp.presentation.elm.reducers

import com.katerina.todoapp.domain.models.TaskModel
import com.katerina.todoapp.presentation.base.extensions.addTask
import com.katerina.todoapp.presentation.base.extensions.editTask
import com.katerina.todoapp.presentation.base.extensions.makeTaskDone
import com.katerina.todoapp.presentation.base.extensions.removeTask
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
            state { copy(tasksListStatus = TasksListStatus.ShowingTaskDescription()) }
            effects { +TasksListEffect.NavigateToTaskDescriptionScreen(null) }
        }

        is TasksListEvent.Ui.OnTaskClicked -> {
            state {
                copy(
                    tasksListStatus = TasksListStatus.ShowingTaskDescription(
                        task = event.task,
                        importance = event.task.importance,
                        deadlineDateTimestamp = event.task.deadlineDateTimestamp
                    )
                )
            }

            effects { +TasksListEffect.NavigateToTaskDescriptionScreen(event.taskId) }
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
            val tasks = state.tasks?.removeTask(event.task) ?: state.tasks
            state { copy(tasks = tasks, doneTasksCount = getDoneTasksCount(tasks)) }
        }

        is TasksListEvent.Ui.OnShowOrHideDoneTasksBtnClicked -> {
            state { copy(isShowingDoneTasks = !state.isShowingDoneTasks) }
        }

        is TasksListEvent.Ui.OnDeadlineDateClicked -> {
            val tasksListStatus = state.tasksListStatus as TasksListStatus.ShowingTaskDescription

            state {
                copy(
                    tasksListStatus = TasksListStatus.ShowingTaskDescription(
                        task = tasksListStatus.task,
                        importance = tasksListStatus.importance,
                        deadlineDateTimestamp = event.timestamp
                    )
                )
            }
        }

        is TasksListEvent.Ui.OnImportanceSelected -> {
            val tasksListStatus = state.tasksListStatus as TasksListStatus.ShowingTaskDescription

            state {
                copy(
                    tasksListStatus = TasksListStatus.ShowingTaskDescription(
                        task = tasksListStatus.task,
                        importance = event.importance,
                        deadlineDateTimestamp = tasksListStatus.deadlineDateTimestamp
                    )
                )
            }
        }

        is TasksListEvent.Ui.OnCloseBtnClicked -> {
            state { copy(tasksListStatus = TasksListStatus.ShowingTasks) }
            effects { +TasksListEffect.NavigateToTaskListScreen }
        }

        is TasksListEvent.Ui.OnSaveBtnClicked -> {
            val tasksListStatus = state.tasksListStatus as TasksListStatus.ShowingTaskDescription
            val task = tasksListStatus.task

            val importance = tasksListStatus.importance
            val deadlineDateTimestamp = tasksListStatus.deadlineDateTimestamp

            if (task == null) {
                commands {
                    +TasksListCommand.AddTask(
                        event.taskDescription,
                        importance,
                        deadlineDateTimestamp
                    )
                }

                state { copy(tasksListStatus = TasksListStatus.ShowingTasks) }

            } else {
                val tasks = state.tasks?.editTask(
                    taskId = task.id,
                    text = event.taskDescription,
                    importance = importance,
                    deadlineDateTimestamp
                )

                state { copy(tasks = tasks, tasksListStatus = TasksListStatus.ShowingTasks) }
            }

            effects { +TasksListEffect.NavigateToTaskListScreen }
        }

        is TasksListEvent.Ui.OnDeleteTaskBtnClicked -> {
            val tasksListStatus = state.tasksListStatus as TasksListStatus.ShowingTaskDescription
            val tasks = tasksListStatus.task?.let { state.tasks?.removeTask(it) } ?: state.tasks

            state {
                copy(
                    tasks = tasks,
                    doneTasksCount = getDoneTasksCount(tasks),
                    tasksListStatus = TasksListStatus.ShowingTasks
                )
            }

            effects { +TasksListEffect.NavigateToTaskListScreen }
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

        is TasksListEvent.Internal.AddTaskSuccess -> {
            state { copy(tasks = state.tasks?.addTask(event.task)) }
            effects { +TasksListEffect.ScrollToBeginningOfList }
        }

        is TasksListEvent.Internal.Error -> {
            state { copy(tasksListStatus = TasksListStatus.Failure) }
            effects { +TasksListEffect.ShowSystemMessage(event.errorMessage) }
        }
    }

    private fun getDoneTasksCount(tasks: List<TaskModel>?): Int =
        tasks?.count { it.isDone } ?: 0
}