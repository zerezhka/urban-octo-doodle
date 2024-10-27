package com.example.githubexplorer.main.repository

import com.example.githubexplorer.main.data.GithubRepository
import com.example.githubexplorer.main.data.GithubUser

class RemoteDataSource: DataSource {
    override fun search(query: String): List<GithubUser> {
        TODO("Not yet implemented")
    }

    override fun getRepositories(user: String): List<GithubRepository> {
        TODO("Not yet implemented")
    }
}