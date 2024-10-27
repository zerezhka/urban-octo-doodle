package com.example.githubexplorer.main.di

import com.example.githubexplorer.BuildConfig
import com.example.githubexplorer.main.data.GithubClient
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
    @Provides
    fun provideGithubClient(): GithubClient {
        return GithubClient(clientId = BuildConfig.GITHUB_CLIENT_ID, appId = BuildConfig.GITHUB_APP_ID)
    }
}