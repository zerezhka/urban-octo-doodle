package com.example.githubexplorer.main.data

import kotlinx.serialization.Serializable

@Serializable
data class GithubRepository(val name: String, val url: String)