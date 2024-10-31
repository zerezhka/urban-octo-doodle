package com.example.githubexplorer.main.repository

import com.example.githubexplorer.main.data.GithubRepository
import com.example.githubexplorer.main.data.GithubUser

interface DataSource {
    // todo separate?
    suspend fun search(query: String): List<GithubUser>
    suspend fun getRepositories(user: String): List<GithubRepository>
}