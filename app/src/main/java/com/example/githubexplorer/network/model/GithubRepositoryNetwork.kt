package com.example.githubexplorer.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GithubRepositoryNetwork(
    @SerialName("html_url") val linkToRepo: String,
    @SerialName("name") val name: String,
    @SerialName("owner") val owner: Owner,
    @SerialName("description") val description: String? = null,
    @SerialName("language") val language: String? = null,
    @SerialName("stargazers_count") val stars: Int = 0,
)

@Serializable
data class Owner(
    @SerialName("login") val login:String
)