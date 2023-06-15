package com.katerina.todoapp.presentation.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.katerina.todoapp.R
import com.katerina.todoapp.databinding.FragmentTaskDescriptionBinding
import com.katerina.todoapp.domain.utils.TaskImportance
import com.katerina.todoapp.presentation.base.extensions.createDatePicker
import com.katerina.todoapp.presentation.base.extensions.createDateString
import com.katerina.todoapp.presentation.base.extensions.dateStringToTimestamp
import com.katerina.todoapp.presentation.base.extensions.disable
import com.katerina.todoapp.presentation.base.extensions.enable
import com.katerina.todoapp.presentation.base.extensions.gone
import com.katerina.todoapp.presentation.base.extensions.toDateFormat
import com.katerina.todoapp.presentation.base.extensions.viewBinding
import com.katerina.todoapp.presentation.base.extensions.visible
import com.katerina.todoapp.presentation.base.watchers.CustomTextWatcher
import com.katerina.todoapp.presentation.elm.TasksListStoreHolder
import com.katerina.todoapp.presentation.elm.models.TasksListEffect
import com.katerina.todoapp.presentation.elm.models.TasksListEvent
import com.katerina.todoapp.presentation.elm.models.TasksListState
import com.katerina.todoapp.presentation.elm.models.TasksListStatus
import vivid.money.elmslie.android.base.ElmFragment

/**
 * [TaskDescriptionFragment] наследуется от [ElmFragment] (который, в свою очередь, наследуется от
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
 *
 * @property[dateListener] служит для хранения слушателя [DatePickerDialog] для выбора даты дедлайна.
 *
 * @property[taskDescriptionTextWatcher] служит для слушателя изменений в тексте описания задачи.
 *
 */
class TaskDescriptionFragment :
    ElmFragment<TasksListEvent, TasksListEffect, TasksListState>(R.layout.fragment_task_description) {

    private val binding by viewBinding(FragmentTaskDescriptionBinding::bind) {
        onDestroyCallback()
    }

    private lateinit var taskId: String
    private val args: TaskDescriptionFragmentArgs by navArgs()

    override val storeHolder = TasksListStoreHolder.getStore(lifecycle)
    override val initEvent = TasksListEvent.Ui.Init

    private val dateListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
        binding.tvDeadline.text = createDateString(day, month + 1, year)
            .also {
                store.accept(TasksListEvent.Ui.OnDeadlineDateClicked(dateStringToTimestamp(it)))
            }
    }

    private val taskDescriptionTextWatcher = object : CustomTextWatcher {
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            with(binding) {
                if (s.isNullOrBlank()) { btnSave.disable() }
                else { btnSave.enable() }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        taskId = args.taskId

        requireActivity().onBackPressedDispatcher.addCallback(owner = viewLifecycleOwner) {
            store.accept(TasksListEvent.Ui.OnCloseBtnClicked)
        }

        with(binding) {
            btnClose.setOnClickListener { onCloseBtnClicked() }

            btnSave.apply {
                disable()
                setOnClickListener { onSaveBtnClicked() }
            }

            switchEnableDeadline.setOnCheckedChangeListener { _, isChecked ->
                onSwitchClicked(
                    isChecked
                )
            }

            tvDeadline.setOnClickListener { onDeadlineTvClicked() }
            tvImportance.setOnClickListener { onImportanceTvClicked() }
            tvDelete.setOnClickListener { onDeleteTvClicked() }

            etTaskDescription.addTextChangedListener(taskDescriptionTextWatcher)
        }
    }

    override fun render(state: TasksListState) {
        when (state.tasksListStatus) {
            is TasksListStatus.ShowingTaskDescription -> showTaskDescription(taskId)
            else -> Unit
        }
    }

    override fun handleEffect(effect: TasksListEffect) {
        if (effect is TasksListEffect.NavigateToTaskListScreen) {
            findNavController().popBackStack()
        }
    }

    private fun onCloseBtnClicked() {
        store.accept(TasksListEvent.Ui.OnCloseBtnClicked)
    }

    private fun onSaveBtnClicked() {
        store.accept(TasksListEvent.Ui.OnSaveBtnClicked(binding.etTaskDescription.text.toString()))
    }

    private fun onSwitchClicked(isChecked: Boolean) {
        with(binding) {
            tvDeadline.apply {
                if (isChecked) visible() else gone()
                store.accept(TasksListEvent.Ui.OnDeadlineDateClicked(null))
            }
        }
    }

    private fun onDeadlineTvClicked() {
        requireContext().createDatePicker(dateListener).show()
    }

    private fun onImportanceTvClicked() {
        with(binding) {
            val popupMenu = PopupMenu(requireContext(), tvImportance)
            popupMenu.menuInflater.inflate(R.menu.importance_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.low -> {
                        store.accept(TasksListEvent.Ui.OnImportanceSelected(TaskImportance.LOW))
                        true
                    }

                    R.id.normal -> {
                        store.accept(TasksListEvent.Ui.OnImportanceSelected(TaskImportance.NORMAL))
                        true
                    }

                    R.id.high -> {
                        store.accept(TasksListEvent.Ui.OnImportanceSelected(TaskImportance.HIGH))
                        true
                    }

                    else -> false
                }
            }

            popupMenu.show()
        }
    }

    private fun onDeleteTvClicked() {
        store.accept(TasksListEvent.Ui.OnDeleteTaskBtnClicked)
    }

    private fun showTaskDescription(taskId: String) {
        with(binding) {
            icDelete.apply { if (taskId != "-1") visible() else gone() }
            tvDelete.apply { if (taskId != "-1") visible() else gone() }

            val tasksListStatus =
                store.currentState.tasksListStatus as TasksListStatus.ShowingTaskDescription

            if (tasksListStatus.deadlineDateTimestamp != null) {
                if (tasksListStatus.task?.deadlineDateTimestamp != null) {
                    switchEnableDeadline.isChecked = true
                    onSwitchClicked(true)
                }

                tvDeadline.text =
                    tasksListStatus.deadlineDateTimestamp.toDateFormat(requireContext())
            }

            tasksListStatus.task?.text?.let { text ->
                etTaskDescription.setText(text)
            }

            tvImportance.text = tasksListStatus.importance.getLocalName(requireContext())
        }
    }

    private fun onDestroyCallback() {
        with(binding) {
            etTaskDescription.removeTextChangedListener(taskDescriptionTextWatcher)
        }
    }
}