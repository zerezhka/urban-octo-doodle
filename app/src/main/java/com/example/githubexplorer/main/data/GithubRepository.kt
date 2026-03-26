package com.example.githubexplorer.main.data

import kotlinx.serialization.Serializable

@Serializable
data class GithubRepository(
    val name: String,
    val url: String,
    val owner: String,
    val description: String? = null,
    val language: String? = null,
    val stars: Int = 0,
)