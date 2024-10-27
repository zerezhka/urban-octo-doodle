package com.example.githubexplorer.main.repository

import com.example.githubexplorer.main.data.GithubUser

class SearchRepository (private val localGateway: LocalGateway, private val remoteGateway: RemoteGateway) {
    fun search(query: String): List<GithubUser> {
        return localGateway.search(query).ifEmpty { remoteGateway.search(query)  }
    }
}