package com.example.githubexplorer.downloads.data

import com.example.githubexplorer.main.data.GithubRepository
import com.example.githubexplorer.main.data.GithubUser

data class Download(val status: Status) {

    sealed class Status {
        object NotDownloaded : Status()
        data class Downloaded(val user: GithubUser, val repository: GithubRepository) : Status()
        data class Downloading(
            val user: GithubUser,
            val repository: GithubRepository,
            val size: Int,
            val downloadedSize: Int
        ) : Status()
    }
}