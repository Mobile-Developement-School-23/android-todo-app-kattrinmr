package com.katerina.todoapp.presentation.base.extensions

import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.katerina.todoapp.presentation.base.delegates.ViewBindingDelegate

fun <T : ViewBinding> Fragment.viewBinding(
    viewBindingFactory: (View) -> T,
    onDestroyCallback: (() -> Unit)? = null
) = ViewBindingDelegate(this, viewBindingFactory, onDestroyCallback)