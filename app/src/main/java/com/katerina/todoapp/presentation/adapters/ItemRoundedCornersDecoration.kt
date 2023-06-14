package com.katerina.todoapp.presentation.adapters

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.katerina.todoapp.R

class ItemRoundedCornersDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val cornersBackground =
        ContextCompat.getDrawable(context, R.drawable.bg_item_rounded_corners)

    private val topCornersBackground =
        ContextCompat.getDrawable(context, R.drawable.bg_item_top_rounded_corners)

    private val bottomCornersBackground =
        ContextCompat.getDrawable(context, R.drawable.bg_item_bottom_rounded_corners)

    private val defaultBackground =
        ContextCompat.getDrawable(context, R.drawable.bg_item_default)

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val itemCount = state.itemCount

        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(child)

            when {
                itemCount == 1 -> {
                    cornersBackground?.apply {
                        bounds = getRectForChild(child)
                        draw(c)
                    }
                }

                position == 0 -> {
                    topCornersBackground?.apply {
                        bounds = getRectForChild(child)
                        draw(c)
                    }
                }

                position == itemCount - 1 -> {
                    bottomCornersBackground?.apply {
                        bounds = getRectForChild(child)
                        draw(c)
                    }
                }

                else -> {
                    defaultBackground?.apply {
                        bounds = getRectForChild(child)
                        draw(c)
                    }
                }
            }
        }
    }

    private fun getRectForChild(child: View) =
        Rect(child.left, child.top, child.right, child.bottom)
}
