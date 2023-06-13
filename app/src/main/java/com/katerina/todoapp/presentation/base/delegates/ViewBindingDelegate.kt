package com.katerina.todoapp.presentation.base.delegates

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import java.lang.IllegalStateException
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ViewBindingDelegate<T : ViewBinding>(
    val f: Fragment,
    val factory: (View) -> T,
    val onDestroyCallback: (() -> Unit)?
) : ReadOnlyProperty<Fragment, T> {

    private var binding: T? = null

    init {
        f.lifecycle.addObserver(object : DefaultLifecycleObserver {
            val viewLifecycleOwnerObserver =
                Observer<LifecycleOwner?> { lifecycleOwner ->

                    val viewLifecycleOwner = lifecycleOwner ?: return@Observer

                    viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {

                        override fun onDestroy(owner: LifecycleOwner) {
                            onDestroyCallback?.let { callback -> callback() }
                            binding = null
                        }
                    })
                }

            override fun onCreate(owner: LifecycleOwner) {
                f.viewLifecycleOwnerLiveData.observeForever(
                    viewLifecycleOwnerObserver
                )
            }

            override fun onDestroy(owner: LifecycleOwner) {
                f.viewLifecycleOwnerLiveData.removeObserver(
                    viewLifecycleOwnerObserver
                )
            }
        })
    }

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        val binding = binding
        if (binding != null) return binding

        val lifecycle = f.viewLifecycleOwner.lifecycle

        if (!lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
            throw FragmentViewsAlreadyDestroyedException()
        }

        return factory(thisRef.requireView()).also { this.binding = it }
    }

    class FragmentViewsAlreadyDestroyedException : IllegalStateException() {
        override val message: String
            get() = "Should not attempt to get bindings when fragment views are destroyed."
    }
}