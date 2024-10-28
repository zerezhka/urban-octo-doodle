package com.example.githubexplorer.main.repository

import com.example.githubexplorer.main.data.GithubRepository
import com.example.githubexplorer.main.data.GithubUser
import com.example.githubexplorer.network.GitHubService
import javax.inject.Inject

class RemoteDataSource @Inject constructor(val retrofit: GitHubService) : DataSource {
    override suspend fun search(query: String): List<GithubUser> =
        retrofit.listUsers(query).items

    override suspend fun getRepositories(user: String): List<GithubRepository> =
        retrofit.listRepos(user).items

}