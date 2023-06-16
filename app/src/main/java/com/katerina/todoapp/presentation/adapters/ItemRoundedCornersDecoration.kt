package com.katerina.todoapp.presentation.adapters

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.katerina.todoapp.R

/**
 * [ItemRoundedCornersDecoration] служит для отрисовки фона элементов
 * в зависимости от их расположения в списке задач.
 */
class ItemRoundedCornersDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val cornersBackground =
        ContextCompat.getDrawable(context, R.drawable.bg_item_rounded_corners)

    private val topCornersBackground =
        ContextCompat.getDrawable(context, R.drawable.bg_item_top_rounded_corners)

    private val bottomCornersBackground =
        ContextCompat.getDrawable(context, R.drawable.bg_item_bottom_rounded_corners)

    private val defaultBackground =
        ContextCompat.getDrawable(context, R.drawable.bg_item_default)

    /**
     * itemCount == 1 - единственный эламент в списке, значит, все его углы закругленные
     *
     * position == 0 - первый элемент, значит, оба верних угла закругленные
     *
     * position == itemCount - 1 - последний элемент, значит, оба нижних угла закругленные
     *
     * else - обычный элемент, значит, прямоугольный фон
     */
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val itemCount = state.itemCount

        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(child)

            when {
                itemCount == 1 -> {
                    cornersBackground?.apply {
                        bounds = getChildRect(child)
                        draw(c)
                    }
                }

                position == 0 -> {
                    topCornersBackground?.apply {
                        bounds = getChildRect(child)
                        draw(c)
                    }
                }

                position == itemCount - 1 -> {
                    bottomCornersBackground?.apply {
                        bounds = getChildRect(child)
                        draw(c)
                    }
                }

                else -> {
                    defaultBackground?.apply {
                        bounds = getChildRect(child)
                        draw(c)
                    }
                }
            }
        }
    }

    private fun getChildRect(child: View) =
        Rect(child.left, child.top, child.right, child.bottom)
}
