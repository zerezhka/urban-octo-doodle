package com.example.githubexplorer.main.repository

import com.example.githubexplorer.main.data.GithubRepository
import com.example.githubexplorer.main.data.GithubUser

interface SearchDataSource {
    suspend fun search(query: String): List<GithubUser>
}

interface RepoDataSource {
    suspend fun getRepositories(user: String): List<GithubRepository>
}
