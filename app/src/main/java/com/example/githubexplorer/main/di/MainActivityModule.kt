package com.example.githubexplorer.main.di

import com.example.githubexplorer.BuildConfig
import com.example.githubexplorer.main.data.GithubClient
import com.example.githubexplorer.main.repository.LocalDataSource
import com.example.githubexplorer.main.repository.RemoteDataSource
import com.example.githubexplorer.main.repository.SearchRepository
import com.example.githubexplorer.main.ui.MainActivityPresenter
import com.example.githubexplorer.main.ui.MainScreenContract
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Singleton

@Module
@InstallIn(ActivityComponent::class)
object MainActivityModule {
    @Provides
    fun provideMainScreenPresenter(): MainScreenContract.Presenter {
        return MainActivityPresenter()
    }

    @Singleton
    @Provides
    fun provideGithubClient(): GithubClient {
        return GithubClient(clientId = BuildConfig.GITHUB_CLIENT_ID, appId = BuildConfig.GITHUB_APP_ID)
    }

    @Singleton
    @Provides
    fun provideRepository(localDataSource: LocalDataSource, remoteDataSource: RemoteDataSource): SearchRepository {
        return SearchRepository(localDataSource, remoteDataSource)
    }
    @Provides
    @Singleton
    fun provideLocalDataSource(): LocalDataSource {
        return LocalDataSource()
    }
    @Provides
    @Singleton
    fun provideRemoteDataSource(): RemoteDataSource {
        return RemoteDataSource()
    }
}