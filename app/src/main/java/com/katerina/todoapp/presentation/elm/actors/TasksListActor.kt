package com.katerina.todoapp.presentation.elm.actors

import com.katerina.todoapp.data.usecases.ChangeTaskStatusUseCaseImpl
import com.katerina.todoapp.data.usecases.GetAllTasksUseCaseImpl
import com.katerina.todoapp.domain.usecases.ChangeTaskStatusUseCase
import com.katerina.todoapp.domain.usecases.GetAllTasksUseCase
import com.katerina.todoapp.presentation.elm.models.TasksListCommand
import com.katerina.todoapp.presentation.elm.models.TasksListEvent
import com.katerina.todoapp.presentation.extensions.mapResultEvents
import vivid.money.elmslie.coroutines.Actor

class TasksListActor : Actor<TasksListCommand, TasksListEvent> {

    private val getAllTasksUseCase: GetAllTasksUseCase by lazy { GetAllTasksUseCaseImpl() }
    private val changeTaskStatusUseCase: ChangeTaskStatusUseCase by lazy { ChangeTaskStatusUseCaseImpl() }

    override fun execute(command: TasksListCommand) = when (command) {

        is TasksListCommand.GetAllTasks ->
            getAllTasksUseCase()
                .mapResultEvents(
                    TasksListEvent.Internal::LoadAllTasksSuccess,
                    TasksListEvent.Internal::Error
                )

        is TasksListCommand.ChangeTaskStatus ->
            changeTaskStatusUseCase(
                command.tasks,
                command.taskId,
                command.status
            ).mapResultEvents(
                TasksListEvent.Internal::LoadAllTasksSuccess,
                TasksListEvent.Internal::Error
            )
    }
}