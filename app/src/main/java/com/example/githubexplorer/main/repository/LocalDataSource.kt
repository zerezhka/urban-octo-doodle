package com.example.githubexplorer.main.repository

import com.example.githubexplorer.main.data.GithubRepository
import com.example.githubexplorer.main.data.GithubUser
import com.example.githubexplorer.main.db.dao.ReposDao
import com.example.githubexplorer.main.db.dao.UsersDao
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val reposDao: ReposDao,
    private val usersDao: UsersDao
) : DataSource {
    override suspend fun search(query: String): List<GithubUser> {
        return usersDao.getAllContaining(name = query).map {
            Converter.fromDatabase(it)
        }
    }

    override suspend fun getRepositories(user: String): List<GithubRepository> {
        return reposDao.getReposByUser(user).map {
            Converter.fromDatabase(it)
        }
    }

    suspend fun getReposByUser(string: String): List<GithubRepository> {
        return reposDao.getReposByUser(string).map {
            Converter.fromDatabase(it)
        }
    }

    suspend fun save(users: List<GithubUser>) {
        usersDao.insertAll(users.map { Converter.toDatabase(it) })
    }
}