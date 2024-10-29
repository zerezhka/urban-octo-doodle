package com.example.githubexplorer.main.repository

import com.example.githubexplorer.main.data.GithubUser
import javax.inject.Inject

open class SearchRepository @Inject constructor(
    val localDataSource: LocalDataSource,
    val remoteDataSource: RemoteDataSource
) {
    suspend fun search(query: String): List<GithubUser> {
        return localDataSource.search(query).ifEmpty { remoteDataSource.search(query)
            .also { localDataSource.usersDao.insertAll(it.map { Converter.toDatabase(it) }) }  }
    }
}