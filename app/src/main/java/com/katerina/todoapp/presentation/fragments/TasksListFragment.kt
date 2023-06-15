package com.katerina.todoapp.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
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

/**
 * [TasksListFragment] наследуется от [ElmFragment] (который, в свою очередь, наследуется от
 * обычного [Fragment][androidx.fragment.app.Fragment]) для реализации MVI.
 *
 * В [ElmFragment] передаются [TasksListEvent], [TasksListEffect], [TasksListState].
 *
 * Функция [render] служит для отрисовки Ui в зависимости от состояния экрана.
 *
 * Функция [handleEffect] служит для обработки [TasksListEffect].
 *
 * @property[storeHolder] служит для хранения [store], который в свою очередь управляет состоянием экрана
 * (принимает и управляет [TasksListEvent] с помощью [TasksListActor][com.katerina.todoapp.presentation.elm.actors.TasksListActor]
 * и [TasksListReducer][com.katerina.todoapp.presentation.elm.reducers.TasksListReducer]).
 *
 * @property[initEvent] служит для хранения стартового [TasksListEvent], который вызывается при создании экрана.
 */
class TasksListFragment :
    ElmFragment<TasksListEvent, TasksListEffect, TasksListState>(R.layout.fragment_tasks_list) {

    private val binding by viewBinding(FragmentTasksListBinding::bind) {
        onDestroyCallback()
    }

    private lateinit var toDoAdapter: ToDoAdapter
    private lateinit var touchHelperCallback: ItemTouchHelper.Callback

    override val initEvent = TasksListEvent.Ui.Init

    override val storeHolder = TasksListStoreHolder.getStore(lifecycle)

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
        else -> Unit
    }

    override fun handleEffect(effect: TasksListEffect) = when (effect) {
        is TasksListEffect.ShowSystemMessage -> showSystemMessage(binding.root, effect.message)
        is TasksListEffect.NavigateToTaskDescriptionScreen -> navigateToTaskDescriptionScreen(effect.taskId)
        is TasksListEffect.ScrollToBeginningOfList -> scrollToBeginningOfList()
        else -> Unit
    }

    private fun onDestroyCallback() {
        with(binding) {
            rvTasks.apply {
                adapter = null
            }
        }
    }

    /**
     * Инициализируем адаптер [ToDoAdapter], добавляем:
     * - [ItemTouchHelper] для свайпа и перемещения
     * - [ItemRoundedCornersDecoration] для отрисовки закругленных углов у первого и последнего элемента в списке
     */
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

    private fun onAddTaskBtnClicked() {
        store.accept(TasksListEvent.Ui.AddNewTaskClicked)
    }

    private fun onShowOrHideDoneTasksBtnClicked() {
        store.accept(TasksListEvent.Ui.OnShowOrHideDoneTasksBtnClicked)
    }

    /**
     * Отрисовка Ui в случае загрузки списка задач.
     */
    private fun showLoading() {
        with(binding) {
            progressBarTasks.invisible()
            rvTasks
        }
    }

    /**
     * В будущем здесь будет отрисовка Ui в случае ошибки.
     */
    private fun showFailure() {}

    /**
     * Отрисовка Ui в случае показа списка задач.
     */
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

    private fun navigateToTaskDescriptionScreen(id: String?) {
        findNavController().navigate(
            TasksListFragmentDirections.actionTasksListFragmentToTaskDescriptionFragment()
                .apply {
                    if (id != null) taskId = id
                }
        )
    }

    private fun scrollToBeginningOfList() {
        with(binding) {
            store.currentState.tasks?.let { tasks ->
                toDoAdapter.submitList(tasks) {
                    rvTasks.scrollToPosition(0)
                }
            }
        }
    }

    private fun onTaskCheckboxClicked(taskId: String, status: Boolean) {
        store.accept(TasksListEvent.Ui.OnTaskCheckboxClicked(taskId, status))
    }

    private fun onTaskClicked(taskId: String, task: TaskModel) {
        store.accept(TasksListEvent.Ui.OnTaskClicked(taskId, task))
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
}