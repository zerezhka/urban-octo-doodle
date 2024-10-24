package com.example.githubexplorer.main.ui

import androidx.annotation.StringRes
import com.example.githubexplorer.base.mvp.MvpPresenter
import com.example.githubexplorer.base.mvp.MvpView

interface MainScreenContract {
    interface View : MvpView {
        fun showLoading()
        fun showError()
    }

    interface Presenter : MvpPresenter<View> {
        fun loadScreen()
    }
}
