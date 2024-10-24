package com.example.githubexplorer.base.mvp

import android.os.Bundle
import androidx.activity.ComponentActivity

abstract class BaseMvpActivity<V : MvpView, P : MvpPresenter<V>> : ComponentActivity(), MvpView {

    abstract val presenter: P

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("UNCHECKED_CAST")
        presenter.attach(this as V)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detach()
    }
}
