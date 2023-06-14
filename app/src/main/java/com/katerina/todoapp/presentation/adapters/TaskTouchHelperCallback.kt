package com.katerina.todoapp.presentation.adapters

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.katerina.todoapp.R

class TaskTouchHelperCallback(
    private val adapter: ItemTouchHelperAdapter,
    context: Context
) : ItemTouchHelper.Callback() {

    private val deleteIcon = ContextCompat.getDrawable(context, R.drawable.ic_delete)
    private val doneIcon = ContextCompat.getDrawable(context, R.drawable.ic_done)

    private val deleteBackground = ColorDrawable(ContextCompat.getColor(context, R.color.red))
    private val doneBackground = ColorDrawable(ContextCompat.getColor(context, R.color.green))

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END

        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun isItemViewSwipeEnabled(): Boolean = true
    override fun isLongPressDragEnabled(): Boolean = true

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        adapter.onItemMove(viewHolder.absoluteAdapterPosition, target.absoluteAdapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.absoluteAdapterPosition

        when (direction) {
            ItemTouchHelper.START -> adapter.onItemSwipedToLeft(position)
            ItemTouchHelper.END -> adapter.onItemSwipedToRight(position)
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val view = viewHolder.itemView

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            if (dX > 0) {
                val iconTop = view.top + (view.height - doneIcon?.intrinsicHeight!!) / 2
                val iconBottom = iconTop + doneIcon.intrinsicHeight

                doneBackground.setBounds(
                    view.left,
                    view.top,
                    view.left + dX.toInt(),
                    view.bottom
                )

                doneIcon.setBounds(
                    view.left + ICON_MARGIN,
                    iconTop,
                    view.left + ICON_MARGIN + doneIcon.intrinsicWidth,
                    iconBottom
                )

                doneBackground.draw(c)
                doneIcon.draw(c)
            }

            if (dX < 0) {
                val iconTop = view.top + (view.height - deleteIcon?.intrinsicHeight!!) / 2
                val iconBottom = iconTop + deleteIcon.intrinsicHeight

                deleteBackground.setBounds(
                    view.right + dX.toInt(),
                    view.top,
                    view.right,
                    view.bottom
                )

                deleteIcon.setBounds(
                    view.right - ICON_MARGIN - deleteIcon.intrinsicWidth,
                    iconTop,
                    view.right - ICON_MARGIN,
                    iconBottom
                )

                deleteBackground.draw(c)
                deleteIcon.draw(c)
            }
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    companion object {
        const val ICON_MARGIN = 32
    }
}