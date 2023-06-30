package com.katerina.todoapp.presentation.elm.reducers

import com.katerina.todoapp.domain.models.TaskModel
import com.katerina.todoapp.presentation.elm.models.TasksListCommand
import com.katerina.todoapp.presentation.elm.models.TasksListEffect
import com.katerina.todoapp.presentation.elm.models.TasksListEvent
import com.katerina.todoapp.presentation.elm.models.TasksListState
import com.katerina.todoapp.presentation.elm.models.TasksListStatus
import javax.inject.Inject
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer

/**
 * [TasksListReducer] служит для управления состояния экрана в зависимости от событий [TasksListEvent], которые происходят на экране.
 *
 * С помощью функции [reduce] происходит обработка событий:
 *
 * - Изменение состояние экрана, т.е. изменение [TasksListState], происходит с помощью копирования дата класса.
 * - Вызов [TasksListEffect] происходит с помощью effects { +[TasksListEffect] }.
 * - Вызов [TasksListCommand] происходит с помощью commands { +[TasksListCommand] }.
 *
 */
class TasksListReducer @Inject constructor() :
    DslReducer<TasksListEvent, TasksListState, TasksListEffect, TasksListCommand>() {

    /**
     * Описание всех событий (возможно, лучше сначала посмотреть код, а в случае недопонимания прочитать это описание):
     *
     * СОБЫТИЯ UI НА ЭКРАНЕ [TasksListFragment][com.katerina.todoapp.presentation.fragments.TasksListFragment]
     *
     * - [TasksListEvent.Ui.Init] - стартовое событие, которое происходит в момент создания экрана.
     *
     * - [TasksListEvent.Ui.AddNewTaskClicked] - событие нажатия на кнопку добавления новой задачи (добавление).
     *
     * - [TasksListEvent.Ui.OnTaskClicked] - событие нажатия на задачу в списке (редактирование).
     *
     * - [TasksListEvent.Ui.OnTaskCheckboxClicked] - событие нажатия на чекбокс задачи.
     *
     * - [TasksListEvent.Ui.OnTaskDragged] - событие перемещения задачи по списку при длительном клике.
     *
     * - [TasksListEvent.Ui.OnTaskSwipedToBeDone] - событие свайпа задачи вправо (чтобы отметить, что она сделана).
     *
     * - [TasksListEvent.Ui.OnTaskSwipedToBeRemoved] - событие свайпа задачи влево (чтобы удалить).
     *
     * - [TasksListEvent.Ui.OnShowOrHideDoneTasksBtnClicked] - события нажатия на кнопку показа / скрытия сделанных задач.
     *
     * СОБЫТИЯ UI НА ЭКРАНЕ [TaskDescriptionFragment][com.katerina.todoapp.presentation.fragments.TaskDescriptionFragment]
     *
     * - [TasksListEvent.Ui.OnDeadlineDateClicked] - событие нажатия на выбор даты дедлайна.
     *
     * - [TasksListEvent.Ui.OnImportanceSelected] - событие нажатия на выбор важности задачи.
     *
     * - [TasksListEvent.Ui.OnCloseBtnClicked] - событие нажатия на кнопку "Закрыть".
     *
     * - [TasksListEvent.Ui.OnSaveBtnClicked] - событие нажатия на кнопку "Сохранить".
     *
     * - [TasksListEvent.Ui.OnDeleteTaskBtnClicked] - событие нажатия на кнопку "Удалить".
     *
     * СОБЫТИЯ INTERNAL
     *
     * - [TasksListEvent.Internal.LoadAllTasksSuccess] - все задачи успешно получены.
     *
     * - [TasksListEvent.Internal.AddTaskSuccess] - новая задача успешно добавлена.
     *
     * - [TasksListEvent.Internal.RemoveTaskSuccess] - задача успешно удалена.
     *
     * - [TasksListEvent.Internal.EditTaskSuccess] - задача успешно отредактирована.
     *
     * - [TasksListEvent.Internal.Error] - произошла ошибка.
     */
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
            state {
                copy(
                    tasksListStatus = TasksListStatus.ShowingTaskDescription(
                        isCreateNewTask = true,
                        task = createDescriptionTask()
                    )
                )
            }
            effects { +TasksListEffect.NavigateToTaskDescriptionScreen(null) }
        }

        is TasksListEvent.Ui.OnTaskClicked -> {
            state {
                copy(
                    tasksListStatus = TasksListStatus.ShowingTaskDescription(
                        isCreateNewTask = false,
                        task = event.task
                    )
                )
            }

            effects { +TasksListEffect.NavigateToTaskDescriptionScreen(event.taskId) }
        }

        is TasksListEvent.Ui.OnTaskCheckboxClicked -> {
            commands {
                +TasksListCommand.EditTask(event.task.copy(isDone = !event.task.isDone))
            }
        }

        is TasksListEvent.Ui.OnTaskDragged -> {
            state { copy(tasks = event.tasks) }
        }

        is TasksListEvent.Ui.OnTaskSwipedToBeDone -> {
            commands {
                +TasksListCommand.EditTask(event.task.copy(isDone = event.task.isDone))
            }
        }

        is TasksListEvent.Ui.OnTaskSwipedToBeRemoved -> {
            commands { +TasksListCommand.RemoveTask(event.task) }
        }

        is TasksListEvent.Ui.OnShowOrHideDoneTasksBtnClicked -> {
            state { copy(isShowingDoneTasks = !state.isShowingDoneTasks) }
        }

        is TasksListEvent.Ui.OnDeadlineDateClicked -> {
            val tasksListStatus = state.tasksListStatus as TasksListStatus.ShowingTaskDescription
            val task = tasksListStatus.task?.copy(deadlineDateTimestamp = event.timestamp)

            state {
                copy(tasksListStatus = tasksListStatus.copy(task = task))
            }
        }

        is TasksListEvent.Ui.OnImportanceSelected -> {
            val tasksListStatus = state.tasksListStatus as TasksListStatus.ShowingTaskDescription
            val task = tasksListStatus.task?.copy(importance = event.importance)

            state {
                copy(tasksListStatus = tasksListStatus.copy(task = task))
            }
        }

        is TasksListEvent.Ui.OnCloseBtnClicked -> {
            state { copy(tasksListStatus = TasksListStatus.ShowingTasks) }
            effects { +TasksListEffect.NavigateToTaskListScreen }
        }

        is TasksListEvent.Ui.OnSaveBtnClicked -> {
            val tasksListStatus = state.tasksListStatus as TasksListStatus.ShowingTaskDescription
            val task = tasksListStatus.task
            val isCreateNewTask = tasksListStatus.isCreateNewTask

            if (task != null) {
                if (isCreateNewTask) {

                    commands {
                        +TasksListCommand.AddTask(task.copy(text = event.taskDescription))
                    }

                } else {
                    commands {
                        +TasksListCommand.EditTask(task.copy(text = event.taskDescription))
                    }
                }
            }

            state { copy(tasksListStatus = TasksListStatus.ShowingTasks) }
            effects { +TasksListEffect.NavigateToTaskListScreen }
        }

        is TasksListEvent.Ui.OnDeleteTaskBtnClicked -> {
            val tasksListStatus = state.tasksListStatus as TasksListStatus.ShowingTaskDescription

            tasksListStatus.task?.let { task ->
                commands { +TasksListCommand.RemoveTask(task) }
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
            state {
                copy(
                    tasks = event.tasks,
                    doneTasksCount = getDoneTasksCount(event.tasks),
                    tasksListStatus = TasksListStatus.ShowingTasks
                )
            }
        }

        is TasksListEvent.Internal.RemoveTaskSuccess -> {
            state {
                copy(
                    tasks = event.tasks,
                    doneTasksCount = getDoneTasksCount(event.tasks),
                    tasksListStatus = TasksListStatus.ShowingTasks
                )
            }
        }

        is TasksListEvent.Internal.EditTaskSuccess -> {
            state {
                copy(
                    tasks = event.tasks,
                    doneTasksCount = getDoneTasksCount(event.tasks),
                    tasksListStatus = TasksListStatus.ShowingTasks
                )
            }
        }

        is TasksListEvent.Internal.Error -> {
            state { copy(tasksListStatus = TasksListStatus.Failure) }
            effects { +TasksListEffect.ShowSystemMessage(event.errorMessage) }
        }
    }

    private fun createDescriptionTask() = TaskModel(
        id = "",
        text = "",
        isDone = false,
        creationDateTimestamp = 0,
        changeDateTimestamp = 0
    )

    private fun getDoneTasksCount(tasks: List<TaskModel>?): Int =
        tasks?.count { it.isDone } ?: 0
}