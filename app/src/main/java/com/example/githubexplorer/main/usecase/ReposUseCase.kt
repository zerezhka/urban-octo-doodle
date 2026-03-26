package com.example.githubexplorer.main.usecase

import com.example.githubexplorer.main.data.GithubRepository
import com.example.githubexplorer.main.repository.LocalDataSource
import com.example.githubexplorer.main.repository.RemoteDataSource
import javax.inject.Inject

class ReposUseCase @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) {
    suspend fun userRepos(user: String): List<GithubRepository> {
        return localDataSource.getRepositories(user).ifEmpty {
            remoteDataSource.getRepositories(user).also { localDataSource.saveRepos(it) }
        }
    }
}
