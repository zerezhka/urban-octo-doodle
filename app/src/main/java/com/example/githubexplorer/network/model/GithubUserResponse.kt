package com.example.githubexplorer.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GithubUserResponse<T>(
    @SerialName("items") val items: List<T>,
    @SerialName("total_count") val totalCount: Int,
    //todo implement pagination
    @SerialName("incomplete_results") val incompleteResults: Boolean
)
