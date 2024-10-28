package com.example.githubexplorer.main.repository

import com.example.githubexplorer.main.data.GithubRepository
import com.example.githubexplorer.main.data.GithubUser
import com.example.githubexplorer.main.db.model.GithubRepoDB
import com.example.githubexplorer.main.db.model.GithubUserDB
import okhttp3.Response

object Converter {
    fun fromNetwork(response: Response): List<GithubUser> {
        //TODO: Implement this
        return emptyList()
    }

    fun fromDatabase(userDB: GithubUserDB): GithubUser {
        return GithubUser(
            userDB.username,
            userDB.avatarUrl
        )
    }
    fun fromDatabase(repoDB: GithubRepoDB): GithubRepository {
        return GithubRepository(
            repoDB.name,
            "https://github.com/"+repoDB.id
        )
    }
}