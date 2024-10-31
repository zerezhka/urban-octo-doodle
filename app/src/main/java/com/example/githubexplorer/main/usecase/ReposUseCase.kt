package com.example.githubexplorer.main.usecase

import com.example.githubexplorer.main.data.GithubRepository
import com.example.githubexplorer.main.repository.LocalDataSource
import com.example.githubexplorer.main.repository.RemoteDataSource
import javax.inject.Inject

class ReposUseCase @Inject constructor(
    val localDataSource: LocalDataSource,
    val remoteDataSource: RemoteDataSource
) {
    suspend fun userRepos(user: String): List<GithubRepository> {
        return localDataSource.getReposByUser(user).ifEmpty {
            return remoteDataSource.getRepositories(user)
        }
    }
// I wanna use just DownloadManager api, no need suspension
    fun downloadRepos(value: GithubRepository) {
        return remoteDataSource.downloadRepo(value)
    }
}