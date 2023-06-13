package com.katerina.todoapp.presentation.elm

import com.katerina.todoapp.presentation.elm.actors.TasksListActor
import com.katerina.todoapp.presentation.elm.models.TasksListCommand
import com.katerina.todoapp.presentation.elm.models.TasksListEffect
import com.katerina.todoapp.presentation.elm.models.TasksListEvent
import com.katerina.todoapp.presentation.elm.models.TasksListState
import com.katerina.todoapp.presentation.elm.reducers.TasksListReducer
import vivid.money.elmslie.coroutines.ElmStoreCompat

object TasksListStoreHolder {

    @Volatile
    private var store:
            ElmStoreCompat<TasksListEvent, TasksListState, TasksListEffect, TasksListCommand>? = null

    fun getStore() =
        store ?: synchronized(this) {
            store ?: ElmStoreCompat(TasksListState(), TasksListReducer(), TasksListActor())
                .also { store = it }
        }
}