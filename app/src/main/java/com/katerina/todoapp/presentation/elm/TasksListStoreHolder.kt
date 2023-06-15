package com.katerina.todoapp.presentation.elm

import androidx.lifecycle.Lifecycle
import com.katerina.todoapp.presentation.elm.actors.TasksListActor
import com.katerina.todoapp.presentation.elm.models.TasksListEffect
import com.katerina.todoapp.presentation.elm.models.TasksListEvent
import com.katerina.todoapp.presentation.elm.models.TasksListState
import com.katerina.todoapp.presentation.elm.reducers.TasksListReducer
import vivid.money.elmslie.android.storeholder.LifecycleAwareStoreHolder
import vivid.money.elmslie.android.storeholder.StoreHolder
import vivid.money.elmslie.coroutines.ElmStoreCompat

/**
 * Служит для хранения [LifecycleAwareStoreHolder], в котором лежит [store][ElmStoreCompat] для [TasksListFragment][com.katerina.todoapp.presentation.fragments.TasksListFragment]
 * и [TaskDescriptionFragment][com.katerina.todoapp.presentation.fragments.TaskDescriptionFragment], чтобы данные переживали смену конфигурации.
 *
 * В будущем [storeHolder] будет инжектиться с помощью даггера, пока что так.
 */
object TasksListStoreHolder {

    @Volatile
    private var storeHolder:
            StoreHolder<TasksListEvent, TasksListEffect, TasksListState>? = null

    fun getStore(lifecycle: Lifecycle) =
        storeHolder ?: synchronized(this) {
            storeHolder ?: LifecycleAwareStoreHolder(lifecycle) {
                ElmStoreCompat(
                    TasksListState(),
                    TasksListReducer(),
                    TasksListActor()
                )
            }.also { storeHolder = it }
        }
}