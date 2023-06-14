package com.katerina.todoapp.presentation.adapters

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.katerina.todoapp.R
import com.katerina.todoapp.databinding.ItemTaskBinding
import com.katerina.todoapp.domain.models.TaskModel
import com.katerina.todoapp.presentation.base.extensions.invisible
import com.katerina.todoapp.presentation.base.extensions.toDateFormat
import com.katerina.todoapp.presentation.base.extensions.visible
import java.util.Collections

class ToDoAdapter(
    private val onCheckboxClicked: (taskId: String, isChecked: Boolean) -> Unit,
    private val onTaskClicked: (taskId: String) -> Unit,
    private val onTaskSwipedToRight: (task: TaskModel) -> Unit,
    private val onTaskSwipedToLeft: (tasks: List<TaskModel>) -> Unit,
    private val onTaskDragged: (tasks: List<TaskModel>) -> Unit
) : ListAdapter<TaskModel, ToDoAdapter.ToDoViewHolder>(ToDoItemDiffCallback()),
    ItemTouchHelperAdapter {

    class ToDoViewHolder(
        private val binding: ItemTaskBinding,
        private val context: Context,
        private val onCheckboxClicked: (taskId: String, isChecked: Boolean) -> Unit,
        private val onTaskClicked: (taskId: String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(task: TaskModel) {
            with(binding) {

                cbIsDone.apply {
                    isChecked = task.isDone
                    setOnClickListener { onCheckboxClicked(task.id, isChecked) }
                }

                tvTaskText.apply {
                    paintFlags = if (task.isDone) paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    else paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()

                    if (task.isDone) setTextColor(context.getColor(R.color.label_tertiary))
                    else setTextColor(context.getColor(R.color.label_primary))

                    text = task.text
                }

                tvTaskDeadline.invisible()

                task.deadlineDateTimestamp?.let { timestamp ->
                    timestamp.toDateFormat(context).also {
                        tvTaskDeadline.apply {
                            visible()
                            text = it
                        }
                    }
                }

                root.setOnClickListener {
                    onTaskClicked(task.id)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder =
        ToDoViewHolder(
            ItemTaskBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),
            parent.context,
            onCheckboxClicked,
            onTaskClicked
        )


    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        val tasks = currentList.toMutableList()

        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(tasks, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(tasks, i, i - 1)
            }
        }

        onTaskDragged(tasks)
    }

    override fun onItemSwipedToRight(position: Int) {
        val tasks = currentList.toMutableList()
        val task = tasks[position]

        onTaskSwipedToRight(task.copy(isDone = !task.isDone))
    }

    override fun onItemSwipedToLeft(position: Int) {
        val tasks = currentList.toMutableList()

        tasks.removeAt(position)
        onTaskSwipedToLeft(tasks.toList())
    }
}

private class ToDoItemDiffCallback : DiffUtil.ItemCallback<TaskModel>() {
    override fun areItemsTheSame(oldItem: TaskModel, newItem: TaskModel): Boolean =
        oldItem.id == newItem.id


    override fun areContentsTheSame(oldItem: TaskModel, newItem: TaskModel): Boolean =
        oldItem == newItem
}