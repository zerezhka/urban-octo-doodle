package com.example.githubexplorer

import kotlinx.serialization.Serializable

@Serializable
sealed class NavRoute {
    @Serializable
    object UserFinder : NavRoute()

    @Serializable
    data class ReposList(val name: String, val avatar: String?) : NavRoute()

    @Serializable
    object DownloadScreen : NavRoute()
}
