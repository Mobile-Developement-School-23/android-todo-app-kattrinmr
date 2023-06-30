package com.katerina.todoapp.presentation.elm.actors

import com.katerina.todoapp.domain.usecases.AddTaskUseCase
import com.katerina.todoapp.domain.usecases.EditTaskUseCase
import com.katerina.todoapp.domain.usecases.GetAllTasksUseCase
import com.katerina.todoapp.domain.usecases.RemoveTaskUseCase
import com.katerina.todoapp.presentation.elm.models.TasksListCommand
import com.katerina.todoapp.presentation.elm.models.TasksListEvent
import com.katerina.todoapp.presentation.base.extensions.mapResultEvents
import javax.inject.Inject
import vivid.money.elmslie.coroutines.Actor

/**
 * [TasksListActor] служит для управления командами [TasksListCommand].
 *
 * С помощью функции [execute] происходит обработка команд: в зависимости от определенной команды
 * вызывается определенный юзкейс.
 *
 * Результат каждого юзкейса маппится либо в событие, которое означает успех, либо в событие, которое обозначает ошибку
 * с помощью [mapResultEvents][com.katerina.todoapp.presentation.base.extensions.mapResultEvents].
 */
class TasksListActor @Inject constructor(
    private val getAllTasksUseCase: GetAllTasksUseCase,
    private val addTaskUseCase: AddTaskUseCase,
    private val removeTaskUseCase: RemoveTaskUseCase,
    private val editTaskUseCase: EditTaskUseCase
) : Actor<TasksListCommand, TasksListEvent> {

    /**
     * Описание команд:
     * - [TasksListCommand.GetAllTasks] - команда получения всех задач.
     * - [TasksListCommand.AddTask] - команда добавления новой задачи.
     * - [TasksListCommand.RemoveTask] - команда удаления задачи.
     * - [TasksListCommand.EditTask] - команда редактирования задачи.
     */
    override fun execute(command: TasksListCommand) = when (command) {

        is TasksListCommand.GetAllTasks ->
            getAllTasksUseCase()
                .mapResultEvents(
                    TasksListEvent.Internal::LoadAllTasksSuccess,
                    TasksListEvent.Internal::Error
                )

        is TasksListCommand.AddTask ->
            addTaskUseCase(command.task)
                .mapResultEvents(
                    TasksListEvent.Internal::AddTaskSuccess,
                    TasksListEvent.Internal::Error
                )

        is TasksListCommand.RemoveTask ->
            removeTaskUseCase(command.task)
                .mapResultEvents(
                    TasksListEvent.Internal::RemoveTaskSuccess,
                    TasksListEvent.Internal::Error
                )

        is TasksListCommand.EditTask ->
            editTaskUseCase(command.task)
                .mapResultEvents(
                    TasksListEvent.Internal::EditTaskSuccess,
                    TasksListEvent.Internal::Error
                )
    }
}