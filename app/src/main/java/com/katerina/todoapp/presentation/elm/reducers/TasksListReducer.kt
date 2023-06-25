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
class TasksListReducer
    : DslReducer<TasksListEvent, TasksListState, TasksListEffect, TasksListCommand>() {

    /**
     * Описание всех событий (возможно, лучше сначала посмотреть код, а в случае недопонимания прочитать это описание):
     *
     * СОБЫТИЯ UI НА ЭКРАНЕ [TasksListFragment][com.katerina.todoapp.presentation.fragments.TasksListFragment]
     *
     * - [TasksListEvent.Ui.Init] - стартовое событие, которое происходит в момент создания экрана.
     *
     * Проверяется [TasksListStatus] состояние, и если в этот момент происходит загрузка данные [TasksListStatus.Loading],
     * то вызывается команда [TasksListCommand.GetAllTasks]. В остальных случаях ничего не происходит.
     *
     * - [TasksListEvent.Ui.AddNewTaskClicked] - событие нажатия на кнопку добавления новой задачи (добавление).
     *
     * Статус [TasksListStatus] состояния [TasksListState] переходит в [TasksListStatus.ShowingTaskDescription],
     * вызывается [TasksListEffect] навигации на экран [TaskDescriptionFragment][com.katerina.todoapp.presentation.fragments.TaskDescriptionFragment]
     * c аргументом null (так как задача новая, а не существующая).
     *
     * - [TasksListEvent.Ui.OnTaskClicked] - событие нажатия на задачу в списке (редактирование).
     *
     * Статус [TasksListStatus] состояния [TasksListState] переходит в [TasksListStatus.ShowingTaskDescription],
     * вызывается [TasksListEffect] навигации на экран [TaskDescriptionFragment][com.katerina.todoapp.presentation.fragments.TaskDescriptionFragment]
     * c информацией по существующей задаче.
     *
     * - [TasksListEvent.Ui.OnTaskCheckboxClicked] - событие нажатия на чекбокс задачи.
     *
     * Вызывается команда [TasksListCommand.ChangeTaskStatus], которая меняет статус задачи на выполненный или не выполненный.
     * Результат выполнения этой команды придет в [TasksListEvent.Internal.LoadAllTasksSuccess] c обновленным списком задач
     * в случае успеха или в [TasksListEvent.Internal.Error] в случае ошибки.
     *
     * - [TasksListEvent.Ui.OnTaskDragged] - событие перемещения задачи по списку при длительном клике.
     *
     * Меняем список задач на тот, что приходит от события (обработка перемещения происходит в адаптере [ToDoAdapter][com.katerina.todoapp.presentation.adapters].
     *
     * - [TasksListEvent.Ui.OnTaskSwipedToBeDone] - событие свайпа задачи вправо (чтобы отметить, что она сделана).
     *
     * Меняем статус задачи на выполненный или наоборот, а с помощью [getDoneTasksCount] считаем количество сделанных задач.
     * В будущем эти действия будут вынесены в репозиторий.
     *
     * - [TasksListEvent.Ui.OnTaskSwipedToBeRemoved] - событие свайпа задачи влево (чтобы удалить).
     *
     * Аналогично [TasksListEvent.Ui.OnTaskSwipedToBeDone], только происходит удаление задачи.
     * В будущем эти действия будут вынесены в репозиторий.
     *
     * - [TasksListEvent.Ui.OnShowOrHideDoneTasksBtnClicked] - события нажатия на кнопку показа / скрытия сделанных задач.
     *
     * Меняем isShowingDoneTasks на противоположный.
     *
     * _
     *
     * _
     *
     * СОБЫТИЯ UI НА ЭКРАНЕ [TaskDescriptionFragment][com.katerina.todoapp.presentation.fragments.TaskDescriptionFragment]
     *
     * - [TasksListEvent.Ui.OnDeadlineDateClicked] - событие нажатия на выбор даты дедлайна.
     *
     * Обновляем [TasksListStatus.ShowingTaskDescription], добавляя новую дату дедлайна.
     *
     * - [TasksListEvent.Ui.OnImportanceSelected] - событие нажатия на выбор важности задачи.
     *
     * Обновляем [TasksListStatus.ShowingTaskDescription], добавляя новое значение важности.
     *
     * - [TasksListEvent.Ui.OnCloseBtnClicked] - событие нажатия на кнопку "Закрыть".
     *
     * Меняем tasksListStatus на [TasksListStatus.ShowingTasks] и
     * вызываем [TasksListEffect.NavigateToTaskListScreen] навигации на экран со списком задач.
     *
     * - [TasksListEvent.Ui.OnSaveBtnClicked] - событие нажатия на кнопку "Сохранить".
     *
     * Сначала проверяется на null [TasksListStatus.ShowingTaskDescription.task].
     *
     * Если null, то добавляем новую задачу: вызывается [TasksListCommand.AddTask], результат
     * придет в [TasksListEvent.Internal.AddTaskSuccess] в случае успеха
     * и в [TasksListEvent.Internal.Error] в случае ошибки; статус меняется на [TasksListStatus.ShowingTasks].
     *
     * Если НЕ null, то редактируем существующую задачу: редактируем задачу, меняем tasks на новый (с отредактированной задачей),
     * меняем статус на [TasksListStatus.ShowingTasks].
     *
     * В обоих случаях далее вызывается [TasksListEffect.NavigateToTaskListScreen] навигации на экран со списком задач.
     *
     * - [TasksListEvent.Ui.OnDeleteTaskBtnClicked] - событие нажатия на кнопку "Удалить".
     *
     * Удаляем задачу из списка, заменяем tasks на новый. Заново считаем количество выполненных задач
     * с помощью [getDoneTasksCount].
     *
     * Меняем статус на [TasksListStatus.ShowingTasks]
     * и вызываем [TasksListEffect.NavigateToTaskListScreen] навигации на экран со списком задач.
     *
     * В будущем удаление задачи будет вынесено в репозиторий.
     *
     * _
     *
     * _
     *
     * СОБЫТИЯ INTERNAL
     *
     * - [TasksListEvent.Internal.LoadAllTasksSuccess] - все задачи успешно получены.
     *
     * Обновляем tasks на тот, что пришел, а также снова считаем количество сделанных задач
     * с помощью [getDoneTasksCount]. Также меняем статус на [TasksListStatus.ShowingTasks].
     *
     * - [TasksListEvent.Internal.AddTaskSuccess] - новая задача успешно добавлена.
     *
     * Добавляем задачу, которая пришла из репозитория и меняем tasks на новый.
     * Вызываем [TasksListEffect.ScrollToBeginningOfList], чтобы было видно новую задачу.
     *
     * - [TasksListEvent.Internal.Error] - произошла ошибка.
     *
     * Меняем tasksListStatus на [TasksListStatus.Failure]
     * и вызываем [TasksListEffect.ShowSystemMessage] для показа снэкбара с сообщением об ошибки.
     *
     * Сейчас [TasksListStatus.Failure] по сути ничего не делает, так как приходят стабовые данные,
     * в будущем наличие этого статуса будет иметь смысл (ошибки от сети и т.п.).
     *
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
                        +TasksListCommand.AddTask(
                            event.taskDescription,
                            task.importance,
                            task.deadlineDateTimestamp
                        )
                    }

                    state { copy(tasksListStatus = TasksListStatus.ShowingTasks) }

                } else {
                    val tasks = state.tasks?.editTask(
                        taskId = task.id,
                        text = event.taskDescription,
                        importance = task.importance,
                        deadlineDateTimestamp = task.deadlineDateTimestamp
                    )

                    state { copy(tasks = tasks, tasksListStatus = TasksListStatus.ShowingTasks) }
                }
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