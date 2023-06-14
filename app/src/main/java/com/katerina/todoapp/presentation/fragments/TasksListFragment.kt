package com.katerina.todoapp.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import com.katerina.todoapp.R
import com.katerina.todoapp.databinding.FragmentTasksListBinding
import com.katerina.todoapp.domain.models.TaskModel
import com.katerina.todoapp.presentation.adapters.ItemRoundedCornersDecoration
import com.katerina.todoapp.presentation.adapters.TaskTouchHelperCallback
import com.katerina.todoapp.presentation.adapters.ToDoAdapter
import com.katerina.todoapp.presentation.elm.TasksListStoreHolder
import com.katerina.todoapp.presentation.elm.models.TasksListEffect
import com.katerina.todoapp.presentation.elm.models.TasksListEvent
import com.katerina.todoapp.presentation.elm.models.TasksListState
import com.katerina.todoapp.presentation.elm.models.TasksListStatus
import com.katerina.todoapp.presentation.base.extensions.gone
import com.katerina.todoapp.presentation.base.extensions.invisible
import com.katerina.todoapp.presentation.base.extensions.showSystemMessage
import com.katerina.todoapp.presentation.base.extensions.viewBinding
import com.katerina.todoapp.presentation.base.extensions.visible
import com.katerina.todoapp.presentation.base.mappers.toUndoneTasks
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.android.storeholder.LifecycleAwareStoreHolder
import vivid.money.elmslie.android.storeholder.StoreHolder

class TasksListFragment :
    ElmFragment<TasksListEvent, TasksListEffect, TasksListState>(R.layout.fragment_tasks_list) {

    private val binding by viewBinding(FragmentTasksListBinding::bind) {
        onDestroyCallback()
    }

    private lateinit var toDoAdapter: ToDoAdapter
    private lateinit var touchHelperCallback: ItemTouchHelper.Callback

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
            btnShowOrHideDoneTasks.setOnClickListener { onShowOrHideDoneTasksBtnClicked() }
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

    private fun onDestroyCallback() {
        with(binding) {
            rvTasks.apply {
                adapter = null
            }
        }
    }

    private fun initTasksRecyclerView() {
        toDoAdapter = ToDoAdapter(
            requireContext(),
            this@TasksListFragment::onTaskCheckboxClicked,
            this@TasksListFragment::onTaskClicked,
            this@TasksListFragment::onTaskSwipedToBeDone,
            this@TasksListFragment::onTaskSwipedToBeRemoved,
            this@TasksListFragment::onTaskDragged
        )

        touchHelperCallback = TaskTouchHelperCallback(toDoAdapter, requireContext())

        with(binding) {
            rvTasks.apply {
                adapter = toDoAdapter
                addItemDecoration(ItemRoundedCornersDecoration(requireContext()))
                setItemViewCacheSize(0)
            }

            ItemTouchHelper(touchHelperCallback).apply {
                attachToRecyclerView(rvTasks)
            }
        }
    }

    private fun showTasks(tasks: List<TaskModel>?) {
        val undoneTasks = tasks?.toUndoneTasks()
        val isShowingDoneTasks = store.currentState.isShowingDoneTasks

        with(binding) {
            rvTasks.invalidateItemDecorations()

            if (undoneTasks?.isEmpty() == true && !isShowingDoneTasks) {
                tvTodoListEmpty.visible()
            } else {
                tvTodoListEmpty.gone()
            }

            if (isShowingDoneTasks) {
                toDoAdapter.submitList(tasks)
                btnShowOrHideDoneTasks.setImageResource(R.drawable.ic_invisible)
            } else {
                toDoAdapter.submitList(tasks?.toUndoneTasks())
                btnShowOrHideDoneTasks.setImageResource(R.drawable.ic_visible)
            }

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

    private fun onTaskSwipedToBeDone(task: TaskModel) {
        store.accept(TasksListEvent.Ui.OnTaskSwipedToBeDone(task))
    }

    private fun onTaskSwipedToBeRemoved(task: TaskModel) {
        store.accept(TasksListEvent.Ui.OnTaskSwipedToBeRemoved(task))
    }

    private fun onTaskDragged(tasks: List<TaskModel>) {
        store.accept(TasksListEvent.Ui.OnTaskDragged(tasks))
    }

    private fun onShowOrHideDoneTasksBtnClicked() {
        store.accept(TasksListEvent.Ui.OnShowOrHideDoneTasksBtnClicked)
    }
}