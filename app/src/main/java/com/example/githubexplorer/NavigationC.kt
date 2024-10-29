package com.example.githubexplorer

import kotlinx.serialization.Serializable

@Serializable
sealed class NavigationC {
    @Serializable
    object UserFinder: NavigationC()
    @Serializable
    data class ReposList(val userName: String) : NavigationC()
    @Serializable
    object DownloadScreen : NavigationC()
}