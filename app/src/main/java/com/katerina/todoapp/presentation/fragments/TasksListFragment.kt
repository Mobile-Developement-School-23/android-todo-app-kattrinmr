package com.katerina.todoapp.presentation.fragments

import android.os.Bundle
import android.view.View
import com.katerina.todoapp.R
import com.katerina.todoapp.data.utils.tasksListStub
import com.katerina.todoapp.databinding.FragmentTasksListBinding
import com.katerina.todoapp.domain.models.TaskModel
import com.katerina.todoapp.presentation.adapters.ToDoAdapter
import com.katerina.todoapp.presentation.elm.TasksListStoreHolder
import com.katerina.todoapp.presentation.elm.models.TasksListEffect
import com.katerina.todoapp.presentation.elm.models.TasksListEvent
import com.katerina.todoapp.presentation.elm.models.TasksListState
import com.katerina.todoapp.presentation.elm.models.TasksListStatus
import com.katerina.todoapp.presentation.extensions.gone
import com.katerina.todoapp.presentation.extensions.invisible
import com.katerina.todoapp.presentation.extensions.showSystemMessage
import com.katerina.todoapp.presentation.extensions.viewBinding
import com.katerina.todoapp.presentation.extensions.visible
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.android.storeholder.LifecycleAwareStoreHolder
import vivid.money.elmslie.android.storeholder.StoreHolder

class TasksListFragment :
    ElmFragment<TasksListEvent, TasksListEffect, TasksListState>(R.layout.fragment_tasks_list) {

    private val binding by viewBinding(FragmentTasksListBinding::bind)

    private val toDoAdapter: ToDoAdapter by lazy {
        ToDoAdapter(
            this@TasksListFragment::onTaskCheckboxClicked,
            this@TasksListFragment::onTaskClicked
        )
    }

    override val initEvent = TasksListEvent.Ui.Init

    private val tasksListStoreHolder: StoreHolder<TasksListEvent, TasksListEffect, TasksListState>
            by lazy { LifecycleAwareStoreHolder(lifecycle) { TasksListStoreHolder.getStore() } }

    override val storeHolder = tasksListStoreHolder

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTasksRecyclerView()

        with(binding) {
            tvDoneCount.text = store.currentState.doneTasksCount.toString()
            btnAddTask.setOnClickListener { onAddTaskBtnClicked() }
        }
    }

    override fun render(state: TasksListState) = when (state.tasksListStatus) {
        is TasksListStatus.Loading -> showLoading()
        is TasksListStatus.Failure -> showFailure()
        is TasksListStatus.ShowingTasks -> showTasks(state.tasks)
    }

    override fun handleEffect(effect: TasksListEffect) = when (effect) {
        is TasksListEffect.ShowSystemMessage -> showSystemMessage(binding.root, effect.message)

        is TasksListEffect.NavigateToEditTask -> {
            // TODO: Реализовать навигацию на экран изменения дела
            showSystemMessage(binding.root, "NavigateToEditTask")
        }

        is TasksListEffect.NavigateToCreateNewTask -> {
            // TODO: Реализовать навигацию на экран создания нового дела
            showSystemMessage(binding.root, "CreateNewTask")
        }
    }

    private fun initTasksRecyclerView() {
        with(binding) {
            rvTasks.apply {
                adapter = toDoAdapter
                setItemViewCacheSize(0)
            }.also {
                toDoAdapter.submitList(tasksListStub)
            }
        }
    }

    private fun showTasks(tasks: List<TaskModel>?) {
        toDoAdapter.submitList(tasks)

        with(binding) {
            tvDoneCount.text = store.currentState.doneTasksCount.toString()
            progressBarTasks.gone()
            rvTasks.visible()
        }
    }

    private fun showFailure() {
        // TODO: Добавить кнопку "Повторить"
    }

    private fun showLoading() {
        with(binding) {
            progressBarTasks.invisible()
            rvTasks
        }
    }

    private fun onAddTaskBtnClicked() {
        store.accept(TasksListEvent.Ui.AddNewTaskClicked)
    }

    private fun onTaskCheckboxClicked(taskId: String, status: Boolean) {
        store.accept(TasksListEvent.Ui.OnTaskCheckboxClicked(taskId, status))
    }

    private fun onTaskClicked(taskId: String) {
        // TODO: Реализовать клик по делу
        with(binding) {
            showSystemMessage(root, "task clicked")
        }
    }
}