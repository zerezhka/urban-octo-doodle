package com.example.githubexplorer.main.ui

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.githubexplorer.NavigationC
import com.example.githubexplorer.downloads.ui.DownloadsScreen
import com.example.githubexplorer.repos.ui.ReposScreen
import com.example.githubexplorer.search.ui.SearchScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = NavigationC.UserFinder,
        enterTransition = { fadeIn() },
        exitTransition = { fadeOut() },
        modifier = modifier
    ) {
        composable<NavigationC.UserFinder> {
            SearchScreen(navController)
        }
        composable<NavigationC.ReposList> { backStackEntry ->
            val route = backStackEntry.toRoute<NavigationC.ReposList>()
            ReposScreen(name = route.name, avatar = route.avatar)
        }
        composable<NavigationC.DownloadScreen> {
            DownloadsScreen()
        }
    }
}
