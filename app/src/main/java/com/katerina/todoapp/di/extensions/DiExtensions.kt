package com.katerina.todoapp.di.extensions

import androidx.fragment.app.Fragment
import com.katerina.todoapp.ToDoApp
import com.katerina.todoapp.di.AppComponent

fun Fragment.getAppComponent(): AppComponent =
    (requireActivity().application as ToDoApp).appComponent