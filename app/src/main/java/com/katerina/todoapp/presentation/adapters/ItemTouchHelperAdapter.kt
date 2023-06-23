package com.katerina.todoapp.presentation.adapters

interface ItemTouchHelperAdapter {
    fun onItemMove(fromPosition: Int, toPosition: Int)
    fun onItemSwipedToRight(position: Int)
    fun onItemSwipedToLeft(position: Int)
}