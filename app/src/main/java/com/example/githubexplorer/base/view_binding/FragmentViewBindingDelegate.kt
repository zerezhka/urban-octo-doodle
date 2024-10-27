package com.example.shwarz.mycountries.commons.view_binding

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class FragmentViewBindingDelegate<T : ViewBinding>(
    bindingClass: Class<T>,
    private val fragment: Fragment
) : ReadOnlyProperty<Fragment, T> {
    private var binding: T? = null

    private val bindMethod = bindingClass.getMethod("bind", View::class.java)

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        binding?.let { return it }

        if (fragment.viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED).not())
            error("Should not attempt to get bindings when Fragment views are destroyed.")

        fragment.viewLifecycleOwner.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    binding = null
                }
            }
        })

        @Suppress("UNCHECKED_CAST")
        binding = bindMethod.invoke(null, thisRef.requireView()) as T
        return binding as T
    }
}

inline fun <reified T : ViewBinding> Fragment.viewBinding() = FragmentViewBindingDelegate(T::class.java, this)