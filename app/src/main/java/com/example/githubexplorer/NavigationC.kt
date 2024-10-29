package com.example.githubexplorer

import kotlinx.serialization.Serializable

@Serializable
sealed class NavigationC {
    @Serializable
    object UserFinder: NavigationC()
    @Serializable
    object ReposList : NavigationC(){
        val route = "repos"
    }
//    object ReposList: NavigationC()
    @Serializable
    object DownloadScreen : NavigationC()
}