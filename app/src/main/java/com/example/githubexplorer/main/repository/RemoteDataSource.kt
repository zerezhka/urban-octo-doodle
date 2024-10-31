package com.example.githubexplorer.main.repository

import com.example.githubexplorer.downloads.data.Download
import com.example.githubexplorer.main.data.GithubRepository
import com.example.githubexplorer.main.data.GithubUser
import com.example.githubexplorer.network.GitHubService
import javax.inject.Inject

class RemoteDataSource @Inject constructor(val retrofit: GitHubService) : DataSource {
    override suspend fun search(query: String): List<GithubUser> =
        retrofit.listUsers(query).items

    override suspend fun getRepositories(user: String): List<GithubRepository> =
        retrofit.listRepos(user).map { GithubRepository(it.name, it.linkToRepo, Download.Status.NotDownloaded) }

    fun downloadRepo(repository: GithubRepository) {
        // DownloadManagerImpl.download(repository)
    }

}