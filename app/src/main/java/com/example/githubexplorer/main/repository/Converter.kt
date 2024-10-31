package com.example.githubexplorer.main.repository

import com.example.githubexplorer.downloads.data.Download
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

    // todo simplify isDownload?
    fun fromDatabase(repoDB: GithubRepoDB): GithubRepository {
        return GithubRepository(
            repoDB.name,
            "https://github.com/" + repoDB.id,
            isDownloaded =
            if (repoDB.isDownload)
                Download.Status.Downloaded(
                    user = fromDatabase(GithubUserDB(repoDB.owner, repoDB.name)),
                    repository = null
                )
            else Download.Status.NotDownloaded
        )
    }
}