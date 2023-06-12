package com.katerina.todoapp.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.katerina.todoapp.databinding.ItemTaskBinding
import com.katerina.todoapp.domain.models.TaskModel
import com.katerina.todoapp.presentation.extensions.toDateFormat
import com.katerina.todoapp.presentation.extensions.visible

class ToDoAdapter(
    private val onCheckboxClicked: (taskId: String, isChecked: Boolean) -> Unit,
    private val onTaskClicked: (taskId: String) -> Unit
) : ListAdapter<TaskModel, ToDoAdapter.ToDoViewHolder>(ToDoItemDiffCallback()) {

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

                tvTaskText.text = task.text

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
}

private class ToDoItemDiffCallback : DiffUtil.ItemCallback<TaskModel>() {
    override fun areItemsTheSame(oldItem: TaskModel, newItem: TaskModel): Boolean =
        oldItem.id == newItem.id


    override fun areContentsTheSame(oldItem: TaskModel, newItem: TaskModel): Boolean =
        oldItem == newItem
}