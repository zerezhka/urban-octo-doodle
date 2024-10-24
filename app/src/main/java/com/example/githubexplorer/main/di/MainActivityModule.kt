package com.example.githubexplorer.main.di

import com.example.githubexplorer.main.ui.MainActivityPresenter
import com.example.githubexplorer.main.ui.MainScreenContract
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object MainActivityModule {
    @Provides
    fun provideMainScreenPresenter(): MainScreenContract.Presenter {
        return MainActivityPresenter()
    }
}