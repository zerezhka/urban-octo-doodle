package com.example.githubexplorer.network

import com.example.githubexplorer.BuildConfig
import com.example.githubexplorer.main.data.GithubUser
import com.example.githubexplorer.network.model.GithubRepositoryNetwork
import com.example.githubexplorer.network.model.GithubUserResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubService {
    @Headers(
        "Accept: application/vnd.github.v3+json",
        "Authorization: ${BuildConfig.GITHUB_TOKEN}",
        "X-GitHub-Api-Version: 2022-11-28"
    )
    @GET("users/{user}/repos")
    suspend fun listRepos(@Path("user") user: String): List<GithubRepositoryNetwork>


    @Headers(
        "Accept: application/vnd.github.v3+json",
        "Authorization: ${BuildConfig.GITHUB_TOKEN}",
        "X-GitHub-Api-Version: 2022-11-28"
    )
    @GET("search/users")
    suspend fun listUsers(@Query("q")query: String): GithubUserResponse<GithubUser>
}