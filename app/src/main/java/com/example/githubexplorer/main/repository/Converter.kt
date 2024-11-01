package com.example.githubexplorer.main.repository

import com.example.githubexplorer.main.data.GithubRepository
import com.example.githubexplorer.main.data.GithubUser
import com.example.githubexplorer.main.db.model.GithubRepoDB
import com.example.githubexplorer.main.db.model.GithubUserDB

object Converter {
    fun toDatabase(user: GithubUser): GithubUserDB {
        return GithubUserDB(
            user.name,
            user.avatar
        )
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
            "https://github.com/" + repoDB.id,
            owner = repoDB.owner,
        )
    }
}