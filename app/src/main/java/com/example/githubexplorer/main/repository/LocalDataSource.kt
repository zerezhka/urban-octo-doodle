package com.example.githubexplorer.main.repository

import com.example.githubexplorer.main.data.GithubRepository
import com.example.githubexplorer.main.data.GithubUser
import com.example.githubexplorer.main.db.dao.ReposDao
import com.example.githubexplorer.main.db.dao.UsersDao
import javax.inject.Inject

class LocalDataSource @Inject constructor(val reposDao: ReposDao, val usersDao: UsersDao) : DataSource {
    override suspend fun search(query: String): List<GithubUser> {
        //todo get from local db
        return listOf(
            // https://github.com/zerezhka?tab=repositories
            //download url = https://github.com/zerezhka/good_training_language/archive/refs/heads/main.zip
            GithubUser("zerezhka", "https://avatars.githubusercontent.com/u/10316435?v=4"),

            // it's org btw (https://github.com/orgs/tsoding/repositories?type=all)
            GithubUser("tsoding", "https://avatars.githubusercontent.com/u/18597647?v=4")
        )
    }

    override suspend fun getRepositories(user: String): List<GithubRepository> {
        //todo get from local db
        return if (user == "zerezhka") {
            listOf(
                GithubRepository("github-explorer", "https://github.com/zerezhka/urban-octo-doodle"),
                GithubRepository("good_training_language", "https://github.com/zerezhka/good_training_language"),
            )
        } else if (user == "tsoding") {
            listOf(
                GithubRepository("musializer", "https://github.com/tsoding/musializer"),
                GithubRepository("nob", "https://github.com/tsoding/nob.h"),
                GithubRepository("koil", "https://github.com/tsoding/koil"),
                GithubRepository("olive.c", "https://github.com/tsoding/olive.c"),
                GithubRepository("good_training_language", "https://github.com/tsoding/good_training_language"),
                )
        } else {
            reposDao.getRepos().map {
                Converter.fromDatabase(it)
            }
        }
    }
}