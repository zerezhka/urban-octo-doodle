package com.example.githubexplorer.base.mvp

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

abstract class BaseMvpFragment<V : MvpView, P : MvpPresenter<V>>(@LayoutRes contentLayoutId: Int = 0):
    Fragment(contentLayoutId),
    MvpView {

    abstract val presenter: P

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        @Suppress("UNCHECKED_CAST")
        presenter.attach(this as V)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detach()
    }
}
