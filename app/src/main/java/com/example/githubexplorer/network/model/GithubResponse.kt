package com.example.githubexplorer.network.model

import kotlinx.serialization.Serializable

@Serializable
data class GithubResponse<T>(val items: List<T>, val total_count: Int, val incomplete_results: Boolean)
