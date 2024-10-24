package com.example.githubexplorer.base.mvp

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlin.coroutines.CoroutineContext


abstract class BasePresenter<V : MvpView> :
    ViewModel(),
    MvpPresenter<V>,
    CoroutineScope {

    override val coroutineContext: CoroutineContext = SupervisorJob() + Dispatchers.Main.immediate

    protected var view: V? = null
        private set

    @CallSuper
    override fun attach(view: V) {
        if (!isActive) {
            throw IllegalArgumentException("BasePresenter::attach($view) after onCleared()")
        }
        this.view = view
    }

    @CallSuper
    override fun detach() {
        view = null
    }

    override fun onCleared() {
        cancel()
        super.onCleared()
    }
}
