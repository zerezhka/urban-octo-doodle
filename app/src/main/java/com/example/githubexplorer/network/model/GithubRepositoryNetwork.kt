package com.example.githubexplorer.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GithubRepositoryNetwork(
    @SerialName("html_url") val linkToRepo:String,
    @SerialName("name") val name:String,
    @SerialName("owner") val owner: Owner,
    )

@Serializable
data class Owner(
    @SerialName("login") val login:String
)