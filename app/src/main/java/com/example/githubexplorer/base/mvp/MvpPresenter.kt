package com.example.githubexplorer.base.mvp

interface MvpPresenter<V : MvpView> {

    fun attach(view: V)

    fun detach()
}
