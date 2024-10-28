package com.example.githubexplorer.main.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GithubUser(
    @SerialName("login")
    val name : String,
    @SerialName("avatar_url")
    val avatar: String,
)