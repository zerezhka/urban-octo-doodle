package com.example.githubexplorer.main.repository

import com.example.githubexplorer.main.data.GithubUser
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) {
    suspend fun search(query: String): List<GithubUser> {
        return localDataSource.search(query).ifEmpty {
            remoteDataSource.search(query).also { localDataSource.save(it) }
        }
    }
}
