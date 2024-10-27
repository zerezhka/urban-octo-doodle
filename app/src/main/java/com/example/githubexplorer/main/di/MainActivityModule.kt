package com.example.githubexplorer.main.di

import com.example.githubexplorer.BuildConfig
import com.example.githubexplorer.main.data.GithubClient
import com.example.githubexplorer.main.ui.MainActivityViewModel
import com.example.githubexplorer.main.usecase.SearchUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
// provides at whole Application lvl for now, because of OkHttp (and it is singleton)
// todo refactor okhttp to another dagger module
// todo return to ActivityComponent::class
// @InstallIn(ActivityComponent::class)
@InstallIn(SingletonComponent::class)
object MainActivityModule {
    @Provides
    fun provideMainScreenViewModel(usecase: SearchUseCase): MainActivityViewModel {
        return MainActivityViewModel(usecase)
    }

    @Singleton
    @Provides
    fun provideGithubClient(): GithubClient {
        return GithubClient(clientId = BuildConfig.GITHUB_CLIENT_ID, appId = BuildConfig.GITHUB_APP_ID)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }
}