package com.example.githubexplorer.main.repository

import com.example.githubexplorer.main.data.GithubUser
import timber.log.Timber

object Converter {
    fun map(string: String): List<GithubUser> {
        Timber.d("RESPONSE: %s", string)
        Timber.d("WARNING:!!! NOT MAPPING YET!!!")
        return emptyList()
    }
}