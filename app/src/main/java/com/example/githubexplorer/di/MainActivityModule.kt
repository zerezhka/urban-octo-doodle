package com.example.githubexplorer.di

import android.content.Context
import androidx.room.Room
import coil3.ImageLoader
import coil3.request.CachePolicy
import coil3.util.Logger
import com.example.githubexplorer.BuildConfig
import com.example.githubexplorer.main.data.GithubClient
import com.example.githubexplorer.main.db.GithubExploraDatabase
import com.example.githubexplorer.main.db.dao.ReposDao
import com.example.githubexplorer.main.db.dao.UsersDao
import com.example.githubexplorer.main.ui.MainActivityViewModel
import com.example.githubexplorer.main.usecase.SearchUseCase
import com.example.githubexplorer.network.TimberLoggingInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import timber.log.Timber
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
        return GithubClient(
            clientId = BuildConfig.GITHUB_CLIENT_ID,
            appId = BuildConfig.GITHUB_APP_ID
        )
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(TimberLoggingInterceptor())
            .build()
    }

    // Database
    @Singleton
    @Provides
    fun provideUsersDao(
        database: GithubExploraDatabase
    ): UsersDao = database.usersDao()

    @Singleton
    @Provides
    fun provideReposDao(
        database: GithubExploraDatabase
    ): ReposDao = database.reposDao()


    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext
        context: Context
    ): GithubExploraDatabase =
        Room
            .databaseBuilder(context, GithubExploraDatabase::class.java, "github_explorer.db")
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideImageLoader(
        @ApplicationContext
        context: Context,
    ): ImageLoader {
        val debugLogger = object : Logger {
            override var minLevel: Logger.Level = Logger.Level.Info

            override fun log(
                tag: String,
                level: Logger.Level,
                message: String?,
                throwable: Throwable?
            ) {
                if (throwable == null)
                    Timber.d("$tag $level $message ")
                else
                    Timber.e(throwable)
            }

        }
        return ImageLoader.Builder(context)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .logger(debugLogger)
            .build()
    }
}