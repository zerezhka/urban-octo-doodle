package com.example.githubexplorer.main.ui

import com.example.githubexplorer.base.mvp.BasePresenter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityPresenter @Inject constructor() : BasePresenter<MainScreenContract.View>(),
    MainScreenContract.Presenter {
    override fun loadScreen() {
        launch {
            view?.showLoading()
        }
    }

    override fun attach(view: MainScreenContract.View) {
        super.attach(view)
        view.showLoading()
    }

    override fun detach() {
        super.detach()
        coroutineContext.cancel(CancellationException("MainActivityPresenter::detach()"))
    }
}