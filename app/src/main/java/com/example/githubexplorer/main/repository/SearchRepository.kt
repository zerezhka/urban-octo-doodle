package com.example.githubexplorer.main.repository

import com.example.githubexplorer.main.data.GithubUser

class SearchRepository (private val localDataSource: LocalDataSource, private val remoteDataSource: RemoteDataSource) {
    fun search(query: String): List<GithubUser> {
        return localDataSource.search(query).ifEmpty { remoteDataSource.search(query)  }
    }
}