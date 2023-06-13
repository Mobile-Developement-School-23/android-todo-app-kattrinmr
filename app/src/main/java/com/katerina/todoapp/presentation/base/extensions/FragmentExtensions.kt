package com.katerina.todoapp.presentation.base.extensions

import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.katerina.todoapp.presentation.base.delegates.ViewBindingDelegate

inline fun <T: ViewBinding> AppCompatActivity.viewBinding(
    crossinline bindingInflater: (LayoutInflater) -> T
) =
    lazy(LazyThreadSafetyMode.NONE) {
        bindingInflater.invoke(layoutInflater)
    }

fun <T : ViewBinding> Fragment.viewBinding(
    viewBindingFactory: (View) -> T,
    onDestroyCallback: (() -> Unit)? = null
) = ViewBindingDelegate(this, viewBindingFactory, onDestroyCallback)