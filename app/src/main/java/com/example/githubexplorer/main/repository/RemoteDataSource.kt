package com.example.githubexplorer.main.repository

import com.example.githubexplorer.main.data.GithubRepository
import com.example.githubexplorer.main.data.GithubUser
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject

class RemoteDataSource @Inject constructor(val okHttpClient: OkHttpClient): DataSource {
    override suspend fun search(query: String): List<GithubUser> {
        okHttpClient.newCall(
            Request.Builder()
                .url("https://api.github.com/search/users?q=$query")
                .method("GET", null)
                .build()
        ).execute().use { response ->
            if (!response.isSuccessful) throw Exception("Unexpected code $response")
            return Converter.fromNetwork(response.body!!.string())
        }
    }

    override suspend fun getRepositories(user: String): List<GithubRepository> {
        TODO("Not yet implemented")
    }
}