package com.example.githubexplorer.main.repository

import com.example.githubexplorer.main.data.GithubRepository
import com.example.githubexplorer.main.data.GithubUser
import com.example.githubexplorer.network.GitHubService
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val retrofit: GitHubService) : SearchDataSource, RepoDataSource {
    override suspend fun search(query: String): List<GithubUser> =
        retrofit.listUsers(query).items

    override suspend fun getRepositories(user: String): List<GithubRepository> =
        retrofit.listRepos(user).map {
            GithubRepository(
                name = it.name,
                url = it.linkToRepo,
                owner = it.owner.login,
                description = it.description,
                language = it.language,
                stars = it.stars,
            )
        }
}